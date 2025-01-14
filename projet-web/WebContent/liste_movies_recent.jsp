<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>More Movies</title>
<link rel="stylesheet" href="./css/liste_movies.css">
</head>
<body>
	<header>
		<button type="button" onclick="retour()">Retour</button>
	</header>
	<main class="main">
        <h1> Movie&Critics > Films > More Recent Movies</h1>
        <div class="movies-container" id="moreRecentMovies">      
        </div>  
       </main>
   	<script>
    function retour() {
        window.location.href = "home.jsp"; // 导航到注册页面
    }
    
    const baseUrl = 'https://api.themoviedb.org/3';
    const apiKey = 'a5362b38ec62c678acd4737ef5ad21dc'; // 替换为你的TMDB API密钥
    const imgUrl = 'https://image.tmdb.org/t/p/w500';
    
    
    // plus récent movie
    async function fetchAllMovies() {
            let page = 1;
            let totalPages = 1;
            const allMovies = [];
            

            while (page <= totalPages) {
                try {
                    const response = await fetch(baseUrl+'/movie/now_playing?api_key='+apiKey+"&page="+page);
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    const data = await response.json();
                    //console.log(data.results); // 日志每页的数据
                    allMovies.push(...data.results);
                    
                    totalPages = data.total_pages;
                    page++;
                } catch (error) {
                    console.error('Error fetching movies:', error);
                    break;
                }
            }
			console.log(allMovies);
            return allMovies;
        }
    
    function displayMovies(movies, containerId) {
        const container = document.getElementById(containerId);
        container.innerHTML = ''; // 清空容器
		
        if (movies && movies.length > 0) {
            movies.forEach(movie => {
            	const {id, title, poster_path,overview,vote_average}=movie;
            	console.log(title);

                const movieDiv = document.createElement('div');
                movieDiv.classList.add('movie');
                /*<div class="movie">
              		<img src="https://image.tmdb.org/t/p/w500/xf1rEQRi9pZxoN8HfggVnhjOaBb.jpg" alt="Godzilla x Kong: The New Empire" onclick="showMovieDetails({ id: 123, title: 'Godzilla x Kong: The New Empire', imageUrl: 'https://image.tmdb.org/t/p/w500/xf1rEQRi9pZxoN8HfggVnhjOaBb.jpg', rating: 7.3, overview: 'Following their explosive showdown, Godzilla and Kong must reunite against a colossal undiscovered threat hidden within our world, challenging their very existence – and our own.' })">
	            	<div class="movie_info">
                   		<h2>Godzilla x Kong: The New Empire</h2>
	                	<span>7.3</span>
                	</div> 
              		<div class="movie_overview">
                   		<h3>Following their explosive showdown, Godzilla and Kong must reunite against a colossal undiscovered threat hidden within our world, challenging their very existence – and our own.</h3>
                	</div>    
          		</div>*/
                
                const img = document.createElement('img');
                img.src = imgUrl + poster_path;
                img.alt = title;

                const movie_info = document.createElement('div');
                movie_info.classList.add('movie_info');
                const mv_title = document.createElement('h2');
                mv_title.textContent = title;
                console.log(mv_title.textContent);
                const mv_vote = document.createElement('span');
                mv_vote.textContent = vote_average;
				
                movie_info.appendChild(mv_title);
                movie_info.appendChild(mv_vote);
                
                const movie_overview= document.createElement('div');
                movie_overview.classList.add('movie_overview');
                const mv_ow = document.createElement('h3');
                mv_ow.textContent = overview;
                movie_overview.appendChild(mv_ow);
                
                movieDiv.appendChild(img);
                movieDiv.appendChild(movie_info);
                movieDiv.appendChild(movie_overview);
                img.onclick = function() {
                	movieDetail(id);
        		};
                container.appendChild(movieDiv);
            });
        } else {
            container.innerHTML = '<p>No movies found</p>';
        }
    }
    
    async function movieDetail(movieId) {
        try {
            
            window.location.href = "movie.jsp?id="+ movieId; // 跳转到电影详情页面
            //window.location.href = "searchResults.jsp?query=" + query;

        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    window.onload = async function() {
        const allMovies = await fetchAllMovies();
        displayMovies(allMovies, 'moreRecentMovies');
    };
        
    </script>
</body>


</html>