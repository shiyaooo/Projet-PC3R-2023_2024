<%@ page import="java.util.Date" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>More posts</title>
<link rel="stylesheet" href="./css/liste_posts.css">
</head>
<body>
	<header>
		<button type="button" onclick="retour()">Retour</button>
	</header>
	<main class="main">
        <h1> Movie&Critics > Posts > More Recent Posts</h1>
        <form id=postForm action="VotreServlet" method="post">
    		<label for="message">Message:</label><br>
    		<input id="text" type="text" id="message" name="message" required><br>
    		<input type="submit" value="Submit">
		</form>
        <div class="posts-container" id="moreRecentPosts">
        </div>  
       </main>
    <script>
    function retour() {
        window.location.href = "home.jsp";
    }
    
    const data = JSON.parse(localStorage.getItem('data'));
    
    document.getElementById('postForm').addEventListener('submit', function(event) {
        event.preventDefault();
        
        const authorId = data._id.$oid;
        const text = document.getElementById('text').value;
        const date = new Date().toISOString();
		//const likes = [];
		//const comments = [];
		const image = "";
		const json = 
        {		
        		authorId:authorId,
        		text:text,
        		date:date,
        		image:image
        };
		
		fetch('Posts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(json)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => { throw new Error(errorData.error); });
            }
            return response.json();
        })
        .then(data => {
        	let message = 'Le message a été envoyé.';
            alert(message);
            window.location.href = "liste_posts_recent.jsp";
        })
        .catch(response => {
        	document.getElementById('validationMessage').textContent = '';
        	let message = 'Ce username existe déjà.';
            document.getElementById('errorMessage').textContent = message;
        });
    });
    
 	// plus récents posts
    async function fetchAllPosts() {
    	fetch("Posts")
        .then(response => response.json())
        .then(posts => {
        	
        	var postsDiv = document.getElementById("moreRecentPosts");
            postsDiv.innerHTML = "";
            posts.forEach(post => {
                fetch("Users?_id=" + post.authorId)
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    if (data.username) {	// si existe (il faut supprimer posts avec user)
                        var postDiv = document.createElement("div");
                        postDiv.innerHTML = "<h2>" + data.username + "</h2><p>" + post.text + "</p><p>" + new Date(post.date).toLocaleString() + "</p>";
                        postsDiv.appendChild(postDiv);
                    }
                });
            });
                
       	})
        .catch(error => {
        	console.log('Il y a eu une erreur:',error);
        });
   	}
 	
    fetchAllPosts();
    
    </script>
</body>
</html>