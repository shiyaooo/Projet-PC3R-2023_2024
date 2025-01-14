<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Movies Search</title>
<link rel="stylesheet" href="./css/liste_movies.css">

</head>
<body>
	<header>
		<button type="button" onclick="retour()">Retour</button>
	</header>
	<main class="main">
		<h1> Movie&Critics > Films > Search Results</h1>
		<% 
        String query = request.getParameter("query");
        if (query != null && !query.isEmpty()) {
        	try {
                // 调用 TMDb API 进行搜索
                String apiKey = "a5362b38ec62c678acd4737ef5ad21dc";
                int currentPage = 1;
                int totalPages = 1;
                org.json.JSONArray allResults = new org.json.JSONArray();
                
                do{
                    String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + java.net.URLEncoder.encode(query, "UTF-8") +"&page=" + currentPage;
                    //String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + query ;
                	java.net.URL tmdbUrl = new java.net.URL(url);
                	java.net.HttpURLConnection connection = (java.net.HttpURLConnection) tmdbUrl.openConnection();
                	connection.setRequestMethod("GET");
                	//out.println("tmdbUrl: " + connection);
                
             		// 获取 API 响应
                	java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
	                StringBuilder jsonResponse = new StringBuilder();
	                String line;
	                while ((line = reader.readLine()) != null) {
	                    jsonResponse.append(line);
	                }
	                //out.println("jsonResponse: " + jsonResponse);
	                reader.close();
                
                	// 解析 JSON 响应
                	org.json.JSONObject resp = new org.json.JSONObject(jsonResponse.toString());
                	org.json.JSONArray results = resp.getJSONArray("results");
                	 if(results.length()<=0){
                     	out.println("<p>No movie provided.</p>");
                     }
                	totalPages = resp.getInt("total_pages");               	
	                //out.println("results: " + resp.getInt("total_results"));
	                // 将当前页的结果添加到 allResults 中
                    for (int i = 0; i < results.length(); i++) {
                        allResults.put(results.getJSONObject(i));
                    }
                    currentPage++;
                } while (currentPage <= totalPages);
	                
	             out.println("<div class='movies-container' id='moreRecentMovies'>");
                for (int i = 0; i < allResults.length(); i++) {
                    org.json.JSONObject movie = allResults.getJSONObject(i);
                    String posterPath = movie.optString("poster_path", "");
                    String title = movie.getString("title");
                    Float vote = movie.optFloat("vote_average");
                    String overview = movie.getString("overview");
                    out.println("<div class='movie'>");
                    //out.println("<img src='https://image.tmdb.org/t/p/w500" + posterPath + "' alt='" + title + "'>");
                    out.println("<img src='https://image.tmdb.org/t/p/w500" + posterPath + "' alt='" + title + "' onClick='movieDetail(" + movie.getInt("id") + ")'>");
                    out.println("<div class='movie_info'>");
                    out.println("<h2>" +title +"</h2>");
                    out.println("<span>" + vote + "</span>");
                    out.println("</div>");
                    out.println("<div class='movie_overview'>");
                   	out.println("<h3>" + overview +"</h3>");
                	out.println("</div>"); 
                    out.println("</div>");
                }
                out.println("</div>");
        	} catch (Exception e) {
                out.println("<p>Error fetching and displaying movies: " + e.getMessage() + "</p>");
            }
        } else {
            out.println("<p>No search query provided.</p>");
        }                             
    	%>
    	
		
	</main>
	
	<script>
    function retour() {
        window.location.href = "home.jsp"; // 导航到注册页面
    }
    
    const baseUrl = 'https://api.themoviedb.org/3';
    const apiKey = 'a5362b38ec62c678acd4737ef5ad21dc'; // 替换为你的TMDB API密钥
    const imgUrl = 'https://image.tmdb.org/t/p/w500';
    
    async function movieDetail(movieId) {
        try {
            
            window.location.href = "movie.jsp?id="+ movieId; // 跳转到电影详情页面
            //window.location.href = "searchResults.jsp?query=" + query;

        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    

        
    </script>

</body>
</html>