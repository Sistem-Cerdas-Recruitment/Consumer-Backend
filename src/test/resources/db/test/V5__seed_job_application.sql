INSERT INTO public.job_application
(id, created_at, updated_at, experience, interview_chat_history, interview_score, is_relevant, relevance_score, status, cv_id, job_id, user_id)
VALUES(
    '1cc0f4b9-206e-4809-b6f2-1252af69c9d4'::uuid, 
    '2024-06-29 15:29:19.147', 
    '2024-06-30 17:28:34.572', 
    '{"skills": ["ReactJS", "TailwindCSS", "PHP", "Redis", "NodeJS (", "Express )", "MySQL", "PostgreSQL", "Java (", "SOAP )", "Docker", "Typescript", "Python", "C", "C + +", "Ruby", "Go", "Java", "MySQL", "PostgreSQL", "MongoDB", "Redis", "React", "Vue", "Express", "Ruby on Rails", "Go Gin", "TailwindCSS", "ChakraUI", "Docker", "Git", "Jira", "Trello", "Google Cloud Platform ( GCP )", "Netlify", "Vercel"], "educations": [{"GPA": "3.56", "major": "School of Electrical Engineering and Informatics", "campus": "Institut Teknologi Bandung"}], "experiences": [{"end": "2023-08-20", "start": "2023-05-20", "company": "Andalin", "position": "Frontend Developer Intern", "description": "Took an active role in the addition of front-end features and maintenance on Andalin’s three primary websites (customer, \ninternal, and company profile website) using VueJS."}]}'::jsonb, 
    '{ "competencies": [ "Mastered database management system (any DB engine, preferred MongoDB)" ], "chatHistories": [ [ { "answer": "I never faced a significant challenge in one of my previous roles because I never had those experiences", "question": "Can you describe a time when you faced a significant challenge in managing a database system in one of your previous roles?", "confidence": "High Confidence", "backspace_count": 10, "predicted_class": "HUMAN", "letter_click_counts": { "a": 7, "b": 1, "c": 4, "d": 3, "e": 16, "f": 4, "g": 1, "h": 2, "i": 8, "j": 1, "k": 0, "l": 1, "m": 1, "n": 6, "o": 5, "p": 2, "q": 0, "r": 5, "s": 6, "t": 1, "u": 2, "v": 3, "w": 0, "x": 1, "y": 1, "z": 0 }, "main_model_probability": "0.06267603", "secondary_model_prediction": "0.35858210142972347" }, { "answer": "A primary key is a unique identifier of a row within a table. A foreign key connects two tables together by giving the unique id of one table to another table.", "question": "TECHNICAL: Explain the difference between a primary key and a foreign key in a database.", "confidence": "Low Confidence", "backspace_count": 22, "predicted_class": "HUMAN", "letter_click_counts": { "a": 13, "b": 5, "c": 2, "d": 2, "e": 18, "f": 5, "g": 4, "h": 5, "i": 16, "j": 0, "k": 3, "l": 4, "m": 2, "n": 10, "o": 10, "p": 2, "q": 2, "r": 9, "s": 3, "t": 14, "u": 4, "v": 1, "w": 3, "x": 0, "y": 6, "z": 0 }, "main_model_probability": "0.01223947", "secondary_model_prediction": "0.4658954969466826" } ] ], "competencyIndex": 0 }'::jsonb, 
    NULL, 
    false, 
    NULL, 
    2, 
    '2f1b1f18-e26c-442f-938e-90730b0d125d'::uuid, 
    '6cd5cac6-6cb5-4222-a209-6f4ae591a593'::uuid, 
    '21936608-d7da-4b77-8636-90908b1cd022'::uuid);

INSERT INTO public.job_application
(id, created_at, updated_at, experience, interview_chat_history, interview_score, is_relevant, relevance_score, status, cv_id, job_id, user_id)
VALUES(
    '5f52082b-1a23-4a5e-9d64-407529ea172e'::uuid, 
    '2024-06-29 15:29:19.147', 
    '2024-06-30 17:28:34.572', 
    '{"skills": ["ReactJS", "TailwindCSS", "PHP", "Redis", "NodeJS (", "Express )", "MySQL", "PostgreSQL", "Java (", "SOAP )", "Docker", "Typescript", "Python", "C", "C + +", "Ruby", "Go", "Java", "MySQL", "PostgreSQL", "MongoDB", "Redis", "React", "Vue", "Express", "Ruby on Rails", "Go Gin", "TailwindCSS", "ChakraUI", "Docker", "Git", "Jira", "Trello", "Google Cloud Platform ( GCP )", "Netlify", "Vercel"], "educations": [{"GPA": "3.56", "major": "School of Electrical Engineering and Informatics", "campus": "Institut Teknologi Bandung"}], "experiences": [{"end": "2023-08-20", "start": "2023-05-20", "company": "Andalin", "position": "Frontend Developer Intern", "description": "Took an active role in the addition of front-end features and maintenance on Andalin’s three primary websites (customer, \ninternal, and company profile website) using VueJS."}]}'::jsonb, 
    '{ "competencies": [ "Mastered database management system (any DB engine, preferred MongoDB)" ], "chatHistories": [], "competencyIndex": 0 }', 
    NULL, 
    false, 
    NULL, 
    1, 
    '18b3dc99-584a-4dfa-83f7-c1a45cc546fe'::uuid, 
    '6cd5cac6-6cb5-4222-a209-6f4ae591a593'::uuid, 
    'dd3b6b82-c532-4cdc-9429-cc6c3eeea623'::uuid);