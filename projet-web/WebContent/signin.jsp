<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Signin Page</title>
    <link rel="stylesheet" href="./css/signin.css">
</head>
<body class="signin_c">
    <div>
        <h2 class="signin_page">Hello, welcome!</h2>
        <div class="inscript">
            <form id=signinForm action="ServletUsers" method="post">
                <input type="text" id="username" name="username" placeholder="username" required><br><br>
                <input type="text" id="mail" name="mail" placeholder="mail" required><br><br>
                <input type="password" id="password1" name="password1" placeholder="password1" required><br><br>
                <input type="password" id="password2" name="password2" placeholder="password2" required><br><br>
                <label for="birthdate">Date de naissance :</label><input type="date" id="birthday" name="birthday" required>
                <input type="submit" value="Signin">
            </form>
            <div class="deja_inscrit">
                <p>Have an account? <a href="login.jsp">Sign In</a></p>
            </div>
            <div id="errorMessage" style="color: red;"></div>
        </div>
    </div>
    
    <script>
        document.getElementById('signinForm').addEventListener('submit', function(event) {
            event.preventDefault();
            
            const password1 = document.getElementById('password1').value;
            const password2 = document.getElementById('password2').value;
            if (password1!=password2) {
            	document.getElementById('errorMessage').textContent = "Les mots de passe ne sont pas identiques !";
            }
            else {
            	const username = document.getElementById('username').value;
            	const mail = document.getElementById('mail').value;
                const birthday = document.getElementById('birthday').value;
				
                const json = 
                {
                		username:username,
                		mail:mail,
                		password:password1,
                		birthday:birthday
                };
                
                fetch('Users', {
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
                	console.log(data);
                	localStorage.setItem('data', JSON.stringify(data));
                	document.getElementById('errorMessage').textContent = '';
                    alert("Bien connecté!"); // Ou rediriger vers une autre page
                    window.location.href = "home.jsp";
                    <!-- <iframe src="login.jsp" frameborder="0" style="width:100%; height:100vh"></iframe> -->
                })
                .catch(response => {
                	let message = 'Ce username existe déjà.';
                    document.getElementById('errorMessage').textContent = message;
                });
                
            } 
        });
    </script>
</body>
</html>