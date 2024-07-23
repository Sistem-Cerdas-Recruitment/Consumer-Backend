package com.BE;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.List;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.BE.dto.antiCheat.AntiCheatEvaluationDTO;
import com.BE.dto.interview.InterviewEvaluationDTO;
import com.BE.dto.job.matching.MatchingRequestDTO;
import com.BE.services.KafkaConsumerTest;
import com.BE.services.antiCheat.AntiCheatService;
import com.BE.services.job.InterviewService;
import com.BE.services.job.MatchingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class MessageQueueIntegrationTest {
  @Autowired
  private InterviewService interviewService;

  @Autowired
  private AntiCheatService antiCheatService;

  @Autowired
  private MatchingService matchingService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private KafkaConsumerTest consumer;

  @Container
  private static final KafkaContainer kafka = new KafkaContainer(
      DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Test
  public void T_811_whenSendingInterviewEvaluationt_thenMessageReceived()
      throws Exception {

    InterviewEvaluationDTO interviewEvaluationDTO = InterviewEvaluationDTO.builder().jobApplicationId(null)
        .competences(List.of()).build();
    interviewService.evaluteInterview(interviewEvaluationDTO);
    Awaitility.await().atMost(Duration.ofSeconds(10)).until(() -> consumer.getReady());
    String objStr = objectMapper.writeValueAsString(interviewEvaluationDTO).replaceAll("\"", "");
    String data = consumer.getMessage().replaceAll("\"", "").replaceAll("\\\\", "");
    assertEquals(objStr, data);
    consumer.reset();
  }

  @Test
  public void T_821_whenSendingAntiCheatMessage_thenMessageReceived()
      throws Exception {

    AntiCheatEvaluationDTO data = AntiCheatEvaluationDTO.builder().jobApplicationId(null).data(List.of()).build();
    antiCheatService.checkForCheating(data);
    Awaitility.await().atMost(Duration.ofSeconds(10)).until(() -> consumer.getReady());
    String objStr = objectMapper.writeValueAsString(data).replaceAll("\"", "");
    String message = consumer.getMessage().replaceAll("\"", "").replaceAll("\\\\", "");
    assertEquals(objStr, message);
    consumer.reset();
  }

  @Test
  public void T_831_whenMatchingMessage_thenMessageReceived()
      throws Exception {

    MatchingRequestDTO data = new MatchingRequestDTO(null, null);
    matchingService.getMatching(data);
    Awaitility.await().atMost(Duration.ofSeconds(10)).until(() -> consumer.getReady());
    String objStr = objectMapper.writeValueAsString(data).replaceAll("\"", "");
    String message = consumer.getMessage().replaceAll("\"", "").replaceAll("\\\\", "");
    assertEquals(objStr, message);
    consumer.reset();
  }
}
