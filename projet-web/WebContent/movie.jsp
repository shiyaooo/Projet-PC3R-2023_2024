<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Movie</title>
<link rel="stylesheet" href="./css/movie.css">
</head>
	
<body>

<%
    // 从请求参数中获取电影ID
    String movieId = request.getParameter("id");
	//out.println(movieId);

    // 在这里可以根据电影ID从数据库或其他地方获取电影详细信息
    // 假设电影信息存储在一个名为 movie 的 JavaBean 中
   // Movie movie = getMovieDetails(movieId);
%>
	<header>
		<button type="button" onclick="retour()">Retour</button>
	</header>
	<div class="container" id="container">
    
    </div>
<script>

	const movieId = '<%= movieId %>'; // 从 JSP 变量获取电影 ID
	const baseUrl = 'https://api.themoviedb.org/3';
    const apiKey = 'a5362b38ec62c678acd4737ef5ad21dc'; // 替换为你的TMDB API密钥
    const imgUrl = 'https://image.tmdb.org/t/p/w500';
	// Simulate the login state
    const data = JSON.parse(localStorage.getItem('data')); // Change this to true to simulate a logged-in user
	console.log("login?", data);

	function retour() {
        window.location.href = "home.jsp"; // 导航到注册页面
    }
	

	document.addEventListener('DOMContentLoaded', function() {
        if (data!='') {
            document.getElementById('rate-button').style.display = 'inline-block';
            document.getElementById('comment-button').style.display = 'inline-block';
            document.getElementById('login-message').style.display = 'none';
        } else {
            document.getElementById('rate-button').style.display = 'none';
            document.getElementById('comment-button').style.display = 'none';
            document.getElementById('login-message').style.display = 'block';
        }

        var critiques = document.querySelectorAll('.critique');
        critiques.forEach(function(critique) {
            var userId = critique.getAttribute('data-user-id');
            if (parseInt(userId) === currentUserId) {
                critique.querySelector('.delete-button').style.display = 'inline-block';
                critique.querySelector('.edit-button').style.display = 'inline-block';
            } else {
                critique.querySelector('.delete-button').style.display = 'none';
                critique.querySelector('.edit-button').style.display = 'none';
            }
        });
    });

    
    async function favorite(movie) {
        if (data!='') {
            alert('Added to favorites!');
            await ajoutcoll(movie._id.$oid, data._id.$oid);
        } else {
            alert('You must be logged in to favorite.');
        }
    }
    
    
    async function saveMovieToDatabase() {    	
        try {            
			console.log(movieId);
			const response = await fetch('Movies?id_tmdb='+movieId, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
            });
			//console.log(response);

            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            //console.log('Movie saved:', data._id.$oid);
            //return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    async function findIdmovie() {    	
        try {            
			const response = await fetch('Movies?action=find_id&id_tmdb='+movieId, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            });
			//console.log(response);

            if (!response.ok) {
                console.log('Movie:');

                throw new Error('Network response was not ok');
            }
            const data = await response.text(); // 假设服务器返回的是一个字符串形式的 _id
            //console.log('Movie _id:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //findcritiques("664d05d12dd4c865ba32d23f");
    async function findcritiques(id_mv) {    	
        try {            
			const response = await fetch('Critiques?receverId='+id_mv, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            });
			console.log(response);

            if (!response.ok) {
                console.log('Movie:');

                throw new Error('Network response was not ok');
            }
            const data = await response.json(); // 假设服务器返回的是一个字符串形式的 _id
            //console.log('critique:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //deletcritiques("6652b967505b960f7af7a64f")
    async function deletcritiques(id_cr) {    	
        try {            
			const response = await fetch('Critiques?_id='+id_cr, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            });
			console.log(response);

            if (!response.ok) {
                console.log('Movie:');

                throw new Error('Network response was not ok');
            }
            const data = await response.json(); // 假设服务器返回的是一个字符串形式的 _id
            //console.log('critique:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //changecritiques("664e4cc0b3b656450658be4d", "joli") ;
    async function changecritiques(id, updatedCritique) {    	
        try {            
			const response = await fetch('Critiques?_id='+id, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedCritique) // 将更新后的评论作为对象传递给 body
                /*body:  new URLSearchParams({
                    'updatedCritique': updatedCritique
                })*/
            });
			console.log("change",response.body);

            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            const data = await response.json(); // 假设服务器返回的是一个字符串形式的 _id
            //console.log('critique:', data);
            //return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //ecritcritique("664b4cd1060f7632c92c8100", "664e4cc0b3b656450658be4d", "joli") ;
    async function ecritcritique(id_user, id_rec, text) {    	
        try {            
            const response = await fetch('Critiques', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    'receverId': id_rec,
                    'authorId': id_user,
                    'text': text
                })
            });
            console.log("change", response.body);

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json(); // 假设服务器返回的是一个字符串形式的 _id
            console.log('critique:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    async function findmovie(id_mv) { 
    	//console.log(id_mv);
        try {            
			const response = await fetch('Movies?action=movie&_id='+id_mv, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            });
			//console.log(response);

            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            //console.log('Movie saved:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //finduser("66476f668681d525e3e82c57");
    async function finduser(id_user) { 
    	//console.log(id_mv);
        try {            
			const response = await fetch('Users?_id='+id_user, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            });
			//console.log(response);

            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            //console.log('User saved:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //findperson("664ccbf87a122146f1f7f9f0");
    async function findperson(id_pers) { 
    	//console.log(id_mv);
        try {            
			const response = await fetch('Persons?_id='+id_pers, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            });
			//console.log(response);

            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            //console.log('Person saved:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //ajoutcoll("665227216fb9f5110d6ab32b", "66476f668681d525e3e82c57" );
    async function ajoutcoll(id_mv, id_user) { 
    	//console.log(id_mv);
        try {            
			const response = await fetch('Collections?', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    'movieId': id_mv,
                    'userId': id_user,
                })
            });
			//console.log(response);

            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            console.log('Collect saved:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //ajoutvote("665227216fb9f5110d6ab32b", "66476f668681d525e3e82c57", 5 );
    async function ajoutvote(id_mv, id_user, score) { 
    	//console.log(id_mv);
        try {            
			const response = await fetch('Votes?', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    'movieId': id_mv,
                    'userId': id_user,
                    'score': score
                })
            });
			//console.log(response);

            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            //console.log('Vote saved:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //updatemovievote("665227216fb9f5110d6ab32b", "66476f668681d525e3e82c57" );
    async function updatemovievote(id_mv, id_user) { 
    	//console.log(id_mv);
        try {            
			const response = await fetch('Movies?_id='+id_mv + '&userId='+id_user, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                
            });
			//console.log(response);

            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            //console.log('Vote update movie:', data);
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    //updatemoviecritiques("665227216fb9f5110d6ab32b");
    async function updatemoviecritiques(id_mv) { 
    	//console.log(id_mv);
        try {            
			const response = await fetch('Movies?_id='+id_mv , {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                
            });
			//console.log(response);

            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            console.log('Critiques update movie:', data);
            return data;
        } catch (error) {
            console.error('Error saving movie:', error);
        }
    }
    
    
  
    async function finddirector() { 
    	//console.log(id_mv);
    	try{
	    	const response = await fetch(baseUrl+ '/movie/' + movieId + '/credits?api_key='+apiKey);

	        if (!response.ok) {
	            throw new Error('Network response was not ok');
	        }
	        const data = await response.json();
	       	const directors = data.crew.filter(member => member.job === 'Director');
	
	        //console.log('Directors:', directors);
	        return directors;
    	} catch (error) {
        	console.error('Error fetching latest movies:', error);
        }
    }
    
    //findactors();
    async function findactors() { 
    	//console.log(id_mv);
    	try{
	    	const response = await fetch(baseUrl+ '/movie/' + movieId + '/credits?api_key='+apiKey);

	    	if (!response.ok) {
	            throw new Error('Network response was not ok');
	        }

	        const data = await response.json();
	        //console.log('Actors:', data.cast);
	        return data.cast;
	    } catch (error) {
	        console.error('Error fetching actors:', error);
	    }
    }
    
    function showRating() {
        document.getElementById('rating-section').style.display = 'block';
        document.getElementById('comment-section').style.display = 'none';
    }

    function showComment() {
        document.getElementById('rating-section').style.display = 'none';
        document.getElementById('comment-section').style.display = 'block';
    }

    async function submitRating(movie) {
        var rating = document.getElementById('rating-input').value;
        if (rating < 1 || rating > 10) {
            alert('Please enter a valid rating between 1 and 10.');
            return;
        }
        alert('Rating submitted: ' + rating);
        await ajoutvote(movie._id.$oid, data._id.$oid, rating);
        await updatemovievote(movie._id.$oid, data._id.$oid);
        document.getElementById('rating-input').value = '';
        document.getElementById('rating-section').style.display = 'none';
    }

    async function submitComment(movie) {
        var comment = document.getElementById('comment-input').value;
        if (comment.trim() === '') {
            alert('Please enter a valid comment.');
            return;
        }
        alert('Comment submitted: ' + comment);
        console.log('comment: ' + comment);
        await ecritcritique(data._id.$oid, movie._id.$oid, comment);
        await updatemoviecritiques(movie._id.$oid);
        document.getElementById('comment-input').value = '';
        document.getElementById('comment-section').style.display = 'none';
    }
	
 	// 在页面上显示电影
    async function displayMovie(movie, containerId) {
        const container = document.getElementById(containerId);
        container.innerHTML = ''; // 清空容器
        //const {title, time, releaseDate, image, actors, crew, genres, vote, vote_count, overview, countries, language, language} = movie;          	
        const movieDetailsDiv = document.createElement('div');
        movieDetailsDiv.classList.add('movie-details');
              
     	// 创建电影海报 img 元素
        const posterImg = document.createElement('img');
        posterImg.src = imgUrl + movie.image;
        posterImg.alt = movie.title;
        posterImg.classList.add('movie-poster');
        
    	// 创建电影标题 h1 元素
        const titleH1 = document.createElement('h1');
        titleH1.classList.add('movie-title');
        titleH1.textContent = movie.title;
        
        movieDetailsDiv.appendChild(posterImg);
        movieDetailsDiv.appendChild(titleH1);
        
        // 创建电影评分 div 元素
        const ratingDiv = document.createElement('div');
        ratingDiv.classList.add('movie-rating');
        ratingDiv.textContent = movie.vote;
        movieDetailsDiv.appendChild(ratingDiv);

        // 创建电影评分数量 div 元素
        const ratingCountDiv = document.createElement('div');
        ratingCountDiv.classList.add('movie-rating');
        ratingCountDiv.textContent = movie.vote_count + ' ratings';
        movieDetailsDiv.appendChild(ratingCountDiv);
        
     	// 创建电影摘要 div 元素
        const summaryDiv = document.createElement('div');
        summaryDiv.classList.add('movie-summary');
        const p1 = document.createElement('p');
        const director = await finddirector();
        //console.log("dire ", director[0].name);
        p1.textContent = "Film By " + director[0].name + ", "+ movie.time +" min   ,  " + movie.releaseDate.$date;
        summaryDiv.appendChild(p1);
        
        const p2 = document.createElement('p');
        p2.textContent = "Genres: "+movie.genres.join(', ');
        summaryDiv.appendChild(p2);
        
        const p3 = document.createElement('p');
        p3.textContent = "Country of Origin: " + movie.countries.join(', ');// +countries.join(', ');
        summaryDiv.appendChild(p3);
        movieDetailsDiv.appendChild(summaryDiv);
        
     	// 创建电影演员 ul 元素
        const movie_actors = document.createElement('div');
        movie_actors.classList.add('movie-actors');
        const casting = document.createElement('h2');
        casting.textContent = "Casting";
        movie_actors.appendChild(casting);
        const actorsUl = document.createElement('ul');
         // 遍历电影演员，并为每个演员创建列表项
     	const actors = await findactors();
        for (const actor of actors) {
            const actorLi = document.createElement('li');				            
            actorLi.textContent = actor.name +" as " +actor.character;
            actorsUl.appendChild(actorLi);
            
        }
        movie_actors.appendChild(actorsUl);
        movieDetailsDiv.appendChild(movie_actors);

        const movie_actions = document.createElement('div');
        movie_actors.classList.add('movie-actions');
     	// 创建收藏按钮
        const favoriteButton = document.createElement('button');
        favoriteButton.textContent = 'Favorite';
        favoriteButton.onclick = async () => {
	        await favorite(movie);
	    };
        movie_actors.appendChild(favoriteButton);
        
     	// 创建评分按钮
        const rateButton = document.createElement('button');
        rateButton.id="rate-button";
        rateButton.style.display = "none";
        rateButton.textContent = 'Rate';
        rateButton.onclick = () => showRating();
        movie_actors.appendChild(rateButton);

        // 创建评论按钮
        const commentButton = document.createElement('button');
        commentButton.id="comment-button";
        commentButton.style.display = "none";
        
        commentButton.textContent = 'Comment';
        commentButton.onclick = () => showComment();
        movie_actors.appendChild(commentButton);
        
        movieDetailsDiv.appendChild(movie_actions);

        if (data!='') {
        	rateButton.style.display = 'inline-block';
        	commentButton.style.display = 'inline-block';
        }else{
        	rateButton.style.display = 'none';
        	commentButton.style.display = 'none';
        }
        	    
    
	 	// 创建评分输入部分
	    const ratingSectionDiv = document.createElement('div');
	    ratingSectionDiv.classList.add('rating');
	    ratingSectionDiv.id = 'rating-section';
	    /*ratingSectionDiv.innerHTML = `
	        <input type="number" id="rating-input" min="1" max="10" placeholder="Enter your rating (1-10)">
	        <button onclick="submitRating(movie)">Submit Rating</button>
	    `;*/
       	const submitButtonR = document.createElement('button');
	    ratingSectionDiv.innerHTML = `
	        <input type="number" id="rating-input" min="1" max="10" placeholder="Enter your rating (1-10)">
	    `;
      	submitButtonR.textContent = 'Submit Rating';
      	submitButtonR.onclick = async () => {
	        await submitRating(movie);
	    };
	    
	    ratingSectionDiv.appendChild(submitButtonR);
	    
	   	movieDetailsDiv.appendChild(ratingSectionDiv);
    	// 创建评论输入部分

        const commentSectionDiv = document.createElement('div');
        commentSectionDiv.classList.add('comment');
        commentSectionDiv.id = 'comment-section';
        commentSectionDiv.style.display = 'none'; // 默认隐藏

    	const submitButton = document.createElement('button');
    	commentSectionDiv.innerHTML += `
            <textarea id="comment-input" rows="4" placeholder="Enter your comment"></textarea>
       		 `;
    	submitButton.textContent = 'Submit Comment';
    	submitButton.onclick = async () => {
	        await submitComment(movie);
	    };
    	commentSectionDiv.appendChild(submitButton);
    	

        /*commentSectionDiv.innerHTML = `
            <textarea id="comment-input" rows="4" placeholder="Enter your comment"></textarea>
            <button onclick="submitComment(movie)">Submit Comment</button>
        `;*/
        movieDetailsDiv.appendChild(commentSectionDiv);  

    	//await ecritcritique(data._id.$oid, movie._id.$oid, value);
    
     	// 创建评论部分
	    const critiquesDiv = document.createElement('div');
	    critiquesDiv.classList.add('critiques');
	    const comments = document.createElement('h2');
	    comments.text = "All Comments";
	    critiquesDiv.appendChild(comments);
	    
        const critiques = await findcritiques(movie._id.$oid);
	    
    	for (const critique of critiques) {
    	    // 创建评论元素
    	    const user =await finduser(critique.authorId);
    	    console.log(user);
    	    const critiqueDiv = document.createElement('div');
    	    critiqueDiv.classList.add('critique');
    	    critiqueDiv.setAttribute('data-user-id', critique.authorId);

    	    // 创建评论内容
    	    const img = document.createElement('img');
    	    img.src = './img/ProfilePics/1.jpg';
    	    img.alt = critique.authorId;

    	    const contentDiv = document.createElement('div');
    	    contentDiv.classList.add('critique-content');

    	    const usernameDiv = document.createElement('div');
    	    usernameDiv.classList.add('critique-username');
    	    usernameDiv.textContent = user.username;

    	    const dateDiv = document.createElement('div');
    	    dateDiv.classList.add('critique-date');
    	    dateDiv.textContent = critique.authorId;

    	    const textDiv = document.createElement('div');
    	    textDiv.classList.add('critique-text');
    	    textDiv.textContent = critique.text;
    	    
    	    const actionscrDiv = document.createElement('div');
    	    actionscrDiv.classList.add('critique-actions');

    	    const deleteButton = document.createElement('button');
    	    deleteButton.classList.add('delete-button');
    	    deleteButton.style.display = 'none'; // 默认隐藏
    	    deleteButton.textContent = 'Delete';
    	    deleteButton.onclick = async () => {
    	        const critiqueId = critique._id; // 获取评论的 ID
    	        console.log(critiqueId);
    	        alert('Delete comment?');
    	        await deletcritiques(critiqueId);
    	    };
    	    
    	    /*const editButton = document.createElement('button');
    	    editButton.classList.add('edit-button');
    	    editButton.style.display = 'none'; // 默认隐藏
    	    editButton.textContent = 'Edit';*/
    	    //editButton.onclick = () => deletcritiques(critique._id.$oid);
    	    
    	    if(critique.authorId == data._id.$oid){
    	    	deleteButton.style.display = 'inline-block';
    	    	//editButton.style.display = 'inline-block';
    	    }else{
        	    deleteButton.style.display = 'none'; // 默认隐藏
        	    //editButton.style.display = 'none'; // 默认隐藏
    	    }
			
    	    
    	    
    	    actionscrDiv.appendChild(deleteButton);
    	    //actionscrDiv.appendChild(editButton);

    	    contentDiv.appendChild(usernameDiv);
    	    contentDiv.appendChild(dateDiv);
    	    contentDiv.appendChild(textDiv);
    	    contentDiv.appendChild(actionscrDiv);

    	    critiqueDiv.appendChild(img);
    	    critiqueDiv.appendChild(contentDiv);

    	    // 将评论元素添加到评论容器中
    	    critiquesDiv.appendChild(critiqueDiv);
    	};
	    
	    movieDetailsDiv.appendChild(critiquesDiv);
        // 将电影详情添加到容器中
        container.appendChild(movieDetailsDiv);

        
    }
    
        
    // appel
    //saveMovieToDatabase()
     async function init() {
         
    	await saveMovieToDatabase();
         
       	const id_mv =await findIdmovie();;
       	const movie = await findmovie(id_mv);
       	
       	await displayMovie(movie, 'container');

     }

    init() // 在页面加载完成后调用 init 函数
</script>


</body>

</body>
</html>