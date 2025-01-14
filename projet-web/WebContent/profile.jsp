<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profil</title>
    <link rel="stylesheet" href="./css/profile.css">
</head>
<body  class="profile_c">
	<header>
		<button type="button" onclick="retour()">Retour</button>
	</header>
	<div class="page">
	<!--
		<div class="navbar">
			<a href="" class="logo-hd">
				<img src="./img/marque.png" alt="logo_site">
			</a>
	        <div class="search-container">
	            <input type="text" id="searchInput" placeholder="Search...">
	            <button type="button" onclick="searchFunction()">Search</button>
	        </div>
	        <div class="isconnect">
		        <button class="inscrire" type="button" onclick="inscrireFunction()">S'INSCRIRE</button>
	        	<button class="connexion" type="button" onclick="connexionFunction()">CONNEXION</button>
	        </div>
    	</div>
    -->
    	
    	<div class="inscript">
    		<h1>Profil</h1>
    		<form id=modifyForm action="Users" method="put">
		        <label for="username">username :</label>
    		    <input type="text" id="username" name="username" value='${data}' placeholder="username" required><br>
        		<label for="email">email :</label>
	        	<input type="text" id="email" name="email" value="${data.email}" placeholder="mail" required><br>
	    	    <label for="password1">password1 :</label>
    	    	<input type="password" id="password1" name="password1" placeholder="password1" required><br>
	    	    <label for="password2">password2 :</label>
    	    	<input type="password" id="password2" name="password2" placeholder="password2" required><br>
	        	<label for="birthdate">Date de naissance :</label>
    	    	<input type="date" id="birthday" name="birthday" value="${data.birthday}" required><br>
       			<input type="submit" value="Enregistrer les modifications">
    		</form>
    		<div id="errorMessage" style="color: red;"></div>
			<div id="validationMessage" style="color: green;"></div> 
    	</div>
    	<button id="deleteButton">SUPPRIMER LE COMPTE</button>
    </div>
    
    <script>
    function retour() {
        window.location.href = "home.jsp";
    }
    
	 // Récupérer les données dans la nouvelle page
    const data = JSON.parse(localStorage.getItem('data'));
    //console.log(data);
    //console.log(data._id.$oid);
    console.log(data.username);
    username.value = data.username;
    email.value = data.mail;
    //console.log(data.birthday);
    birthday.value = data.birthday;
    
	document.getElementById('modifyForm').addEventListener('submit', function(event) {
        event.preventDefault();
    	
        const password1 = document.getElementById('password1').value;
        const password2 = document.getElementById('password2').value;
        if (password1!=password2) {
        	document.getElementById('errorMessage').textContent = "Les mots de passe ne sont pas identiques !";
        }
        else {
        	const username = document.getElementById('username').value;
        	const mail = document.getElementById('email').value;
            const birthday = document.getElementById('birthday').value;
			
            const json = 
            {		
            		_id:data._id.$oid,
            		username:username,
            		mail:mail,
            		password:password1,
            		birthday:birthday
            };
            
            fetch('Users', {
                method: 'PUT',
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
            	localStorage.setItem('data', JSON.stringify(data));
            	//console.log(data);
            	let message = 'Les modifications ont été enregistrées.';
                document.getElementById('validationMessage').textContent = message;
                document.getElementById('errorMessage').textContent = '';
                alert(message);
            })
            .catch(response => {
            	document.getElementById('validationMessage').textContent = '';
            	let message = 'Ce username existe déjà.';
                document.getElementById('errorMessage').textContent = message;
            });
            
        } 
    });
	
	document.getElementById("deleteButton").addEventListener("click", function(){
		// Affiche une boîte de dialogue de confirmation à l'utilisateur
	    var userConfirmed = confirm("Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible.");

	    if (userConfirmed) {
	        // Si l'utilisateur confirme, supprime le compte

		    fetch('Users?_id=' + data._id.$oid, {
		        method: 'DELETE',
	    	})
		    .then(response => {
		        if (!response.ok) {
	    	        throw new Error('Réponse invalide');
	        	}
		        else {
		        	localStorage.setItem('data', '');
		        	window.location.href = "home.jsp";
		        }
		    })
	    	.catch(error => {
	        	console.error('Il y a eu une erreur:', error);
		    });
	    }
	});
    </script>
</body>
</html>