<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
 	<meta charset="UTF-8">
    <title>Movie & Critics</title>
    <link rel="stylesheet" href="./css/home.css">
   
</head>
<body>
	<div class="page">
		<div class="navbar">
			<a href="" class="logo-hd">
				<img src="./img/marque.png" alt="logo_site">
			</a>
	        <div class="search-container">
	            <form id="searchForm" action="DisplaySearchResults.jsp" method="GET" onsubmit="searchMovies(event)">
	            <input type="text" id="searchInput" placeholder="Search...">
	            <button type="submit" onclick="searchFunction(event)">Search</button>
	            </form>
	        </div>
	        <div class="isconnect">
		        <button id="inscrire" class="inscrire" type="button" onclick="inscrireFunction()">S'INSCRIRE</button>
	        	<button id="connexion" class="connexion" type="button" onclick="connexionFunction()">CONNEXION</button>
	        </div>
    	</div>
		<div class="main">	
			<h1>Recent Movies</h1>	
			<button class="more_movies" type="button" onclick="more_movies_recent()">Voir Plus</button>	
            <div class = "movies-container" id="recentMovies"></div>
            
            <div id="recentMovies"></div>
            <h1>Highly-rated movies</h1>
  			<button class="more_movies" type="button" onclick="more_movies_rated()">Voir Plus</button>		
     		<div class = "movies-container" id="popularMovies">
     		</div>
            
			<h1>Popular Post</h1>
			<button class="more_movies" type="button" onclick="more_posts()">Voir Plus</button>	
		</div>
    	
    </div>
    <script>
    
	    function inscrireFunction() {
	        window.location.href = "signin.jsp"; // 导航到注册页面
	    }
	
	    function connexionFunction() {
	        window.location.href = "login.jsp"; // 导航到登录页面
	    }
	    
	    function more_movies_recent(){
	    	 window.location.href = "liste_movies_recent.jsp"; // 导航到注册页面
	    }
	    
	    function more_movies_rated(){
	    	 window.location.href = "liste_movies_rated.jsp"; // 导航到注册页面
	    }
	    
	    function more_posts() {
	    	window.location.href = "liste_posts_recent.jsp";
	    }
    	
	    function showMovieDetails(movie) {
	    	  // 在此处执行显示电影详情的操作，例如将电影详情显示在模态框中或导航到另一个页面
	    	  console.log("Title: " + movie.title);
	    	  console.log("Image URL: " + movie.imageUrl);
	    	  console.log("Rating: " + movie.rating);
	    	  console.log("Overview: " + movie.overview);
	    	  window.location.href = "movie.jsp"; // 导航到注册页面
	    	}
             
        const baseUrl = 'https://api.themoviedb.org/3';
        const apiKey = 'a5362b38ec62c678acd4737ef5ad21dc'; // 替换为你的TMDB API密钥
        const imgUrl = 'https://image.tmdb.org/t/p/w500';
        

        

        // plus récent movie
        async function getLatestMovies() {
            try {
            	//console.log(apiKey);
            	//const response = await fetch(`${baseUrl}/movie/now_playing?api_key=${apiKey}`);
                const response = await fetch(baseUrl+'/movie/now_playing?api_key='+apiKey);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                console.log('Latest Movies:', data.results.slice(0, 5));

                return data.results.slice(0, 5);
            } catch (error) {
                console.error('Error fetching latest movies:', error);
            }
        }
	
        //document.getElementById('searchForm').addEventListener('submit', searchFunction);
        
        
     	// les movie plus populaires
        async function getPopularMovies() {
            try {
                const response = await fetch(baseUrl+"/movie/top_rated?api_key="+apiKey);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                console.log('Popular Movies:', data.results.slice(0, 5));
                return data.results.slice(0, 5);
            } catch (error) {
                console.error('Error fetching popular movies:', error);
            }
        }
     	
        async function searchFunction(event) {
        	event.preventDefault();
            const query = document.getElementById('searchInput').value;
            console.log(query);
            if (!query) {
                alert('Please enter a search term');
                return;
            }
            window.location.href = "searchResults.jsp?query=" + query;
        }

        // 在页面上显示电影
        function displayMovies(movies, containerId) {
            const container = document.getElementById(containerId);
            container.innerHTML = ''; // 清空容器
			
            if (movies && movies.length > 0) {
                movies.forEach(movie => {
                	const {id, title, poster_path,overview,vote_average}=movie;
                	
                    const movieDiv = document.createElement('div');
                    movieDiv.classList.add('movie');
                    /*movieDiv.innerHTML = `
                    	<div class="movie">
                        	<img src="${imgUrl + poster_path}" alt="${title}">
    		            <div class="movie_info">
                        	<h2>${title}</h2>
    		                <span>${vote_average}</span>
  		                </div>
    		           
                    `*/
                    const img = document.createElement('img');
                    img.src = imgUrl + poster_path;
                    img.alt = title;
                    
                    const movie_info = document.createElement('div');
                    movie_info.classList.add('movie_info')
                    const mv_title = document.createElement('h2');
                    mv_title.textContent = title;
                    //console.log(mv_title.textContent);
                    const mv_vote = document.createElement('span');
                    mv_vote.textContent = vote_average;
					
                    movie_info.appendChild(mv_title);
                    movie_info.appendChild(mv_vote);
                    
                    movieDiv.appendChild(img);
                    movieDiv.appendChild(movie_info);
                    
                    //img.onclick = showMovieDetails(movie); // 添加点击事件
                    img.onclick = function() {
                    	movieDetail(id);
            		};
                    container.appendChild(movieDiv);
					console.log(img);

                });
                
             	// 为每张图片添加点击事件监听器
                /*container.querySelectorAll('.movie img').forEach(img => {
                	img.addEventListener('onclick', function() {
                        // 在这里添加你想要执行的操作
                        // 例如跳转到电影详情页面或者显示电影详情的模态框
                        console.log('Image clicked:', img.alt);
                        // 如果要跳转到详情页面，可以调用 showMovieDetails 函数
                        //showMovieDetails(movie);
                });*/
            } else {
                container.innerHTML = '<p>No movies found</p>';
            }
        }
        
        async function movieDetail(movieId) {
            try {
                /*const response = await fetch('/Movies?id_tmdb='+movieId, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(movie)
                });
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                console.log('Movie saved:', data);*/
                window.location.href = "movie.jsp?id="+ movieId; // 跳转到电影详情页面
                //window.location.href = "searchResults.jsp?query=" + query;

            } catch (error) {
                console.error('Error saving movie:', error);
            }
        }
        
    	
     
        // initialiser
        async function init() {
            const popularMovies = await getPopularMovies();
            displayMovies(popularMovies, 'popularMovies');
            
          	const latestMovies = await getLatestMovies();
            displayMovies(latestMovies, 'recentMovies');
            
          	//const searchMovies = await searchFunction();
            //displayMovies(searchMovies, 'searchResults');
        }

        init(); // 在页面加载完成后调用 init 函数

        var button1 = document.getElementById("connexion");
        var button2 = document.getElementById("inscrire");
        const data = JSON.parse(localStorage.getItem('data'));
		console.log("login?", data);
		if (data!='') {
            // Si l'utilisateur est connecté
            button1.innerHTML = "DECONNEXION";
            button1.onclick = function() {
            	localStorage.setItem('data', '');
            	window.location.href = "home.jsp";
            };
            button2.innerHTML = "PROFIL";
            button2.onclick = function() {
            	window.location.href = "profile.jsp";
            };
        } else {
            // Si l'utilisateur n'est pas connecté
            button1.innerHTML = "CONNECTION";
            button1.onclick = connexionFunction();
            button2.innerHTML = "S'INSCRIRE";
            button2.onclick = inscrireFunction();
        }
       	
    </script>
</body>
</html>
