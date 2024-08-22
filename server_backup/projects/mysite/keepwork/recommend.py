# # myapp/views.py

# from rest_framework.response import Response
# from rest_framework.decorators import api_view
# from sklearn.metrics.pairwise import cosine_similarity
# from sklearn.preprocessing import StandardScaler
# import pandas as pd
# import csv
# import mysql.connector

# # MySQL 연결 정보
# host = "keepwork-database.c24vlvvlfcen.ap-northeast-2.rds.amazonaws.com"
# user = "admin"
# password = "npnc2023*"
# port = "3306"
# database = "keepwork_db"

# @api_view(['GET'])
# def recommendations_api(request, user_id):
#     try:
#         # MySQL 연결
#         connection = mysql.connector.connect(
#             host=host,
#             user=user,
#             password=password,
#             port=port,
#             database=database
#         )

#         # 데이터 검색 쿼리
#         query = "SELECT * FROM favorite;"

#         # 쿼리 실행
#         cursor = connection.cursor()
#         cursor.execute(query)
#         result = cursor.fetchall()

#         # CSV 파일로 데이터 쓰기
#         with open('output.csv', 'w', newline='') as csvfile:
#             csv_writer = csv.writer(csvfile)
#             csv_writer.writerow([i[0] for i in cursor.description])  # 헤더 작성
#             csv_writer.writerows(result)  # 데이터 작성

#         # 데이터 불러오기
#         data = pd.read_csv('output.csv')
#         df = pd.DataFrame(data)

#         # 사용자-공고 매트릭스 생성
#         df['application'] = 1
#         user_job_matrix = df.pivot(index='personal_id', columns='job_listnum', values='application').fillna(0).astype(int)

#         # 매트릭스 정규화
#         scaler = StandardScaler()
#         normalized_matrix = scaler.fit_transform(user_job_matrix)

#         # 코사인 유사도 계산
#         user_similarity = cosine_similarity(normalized_matrix)
#         user_similarity_df = pd.DataFrame(user_similarity, index=user_job_matrix.index, columns=user_job_matrix.index)

#         def get_recommendations(user_id, top_n=5):
#             recommendations = []
#             already_recommended = set()

#             # 사용자와 유사도를 내림차순으로 정렬
#             similar_users = user_similarity_df[user_id].sort_values(ascending=False)

#             for sim_user_id, similarity in similar_users.iteritems():
#                 if sim_user_id == user_id:
#                     continue

#                 # 유사한 사용자가 지원한 공고 중 사용자가 아직 지원하지 않은 공고 추천
#                 sim_user_jobs = user_job_matrix.loc[sim_user_id, :]
#                 diff_jobs = sim_user_jobs - user_job_matrix.loc[user_id, :]

#                 for job_num, value in diff_jobs.iteritems():
#                     if value == 1 and user_job_matrix.loc[user_id, job_num] == 0 and job_num not in already_recommended:
#                         recommendations.append((job_num, similarity))
#                         already_recommended.add(job_num)

#                         if len(recommendations) >= top_n:
#                             return recommendations

#             if len(recommendations) < 5:
#                 # 최대 5개를 채우기 위해 다른 유사한 사용자의 추천 공고를 확인
#                 remaining_recommendations = 5 - len(recommendations)
#                 additional_recommendations = []

#                 for sim_user_id, _ in similar_users.iteritems():
#                     if sim_user_id == user_id:
#                         continue

#                     sim_user_jobs = user_job_matrix.loc[sim_user_id, :]
#                     diff_jobs = sim_user_jobs - user_job_matrix.loc[user_id, :]

#                     for job_num, value in diff_jobs.iteritems():
#                         if value == 1 and user_job_matrix.loc[user_id, job_num] == 0 and job_num not in already_recommended:
#                             additional_recommendations.append((job_num, similarity))
#                             already_recommended.add(job_num)

#                             if len(additional_recommendations) >= remaining_recommendations:
#                                 break

#                 recommendations.extend(additional_recommendations)

#             return recommendations

#         top_recommendations = get_recommendations(user_id)

#         # 결과를 API 응답으로 반환
#         response_data = [{'job_num': job_num, 'similarity': similarity} for job_num, similarity in top_recommendations]

#         return Response({'recommendations': response_data})
#     except Exception as e:
#         # 예외 처리
#         return Response({'error': str(e)})
#     finally:
#         if connection.is_connected():
#             cursor.close()
#             connection.close()
