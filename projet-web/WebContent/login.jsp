<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="./css/login.css">
</head>
<body class="login_c">
    <div>
        <h2 class="login_page">Hello, welcome!</h2>
        <div class="inscript">
            <form id=loginForm action="LoginServlet" method="post">
                <input type="text" id="login" name="login" placeholder="username" required><br><br>
                <input type="password" id="password" name="password" placeholder="password" required><br><br>
                <input type="submit" value="Login">
            </form>
            <div>
                <p>Don't have an account? <a href="signin.jsp">Sign Up</a></p>
                <!-- <p>Forgot your password? <a href="forgotpassword.html">Forgot Password</a></p> -->
            </div>
            <div id="errorMessage" style="color: red;"></div>
        </div>
    </div>
    
    <script>
        document.getElementById('loginForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const login = document.getElementById('login').value;
            const password = document.getElementById('password').value;

            fetch('LoginServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    'login': login,
                    'password': password
                })
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(errorData => { throw new Error(errorData.error); });
                }
                return response.json();
            })
            .then(data => {
            	//console.log(data);
            	// Stocker les données dans le stockage local
				localStorage.setItem('data', JSON.stringify(data));
                document.getElementById('errorMessage').textContent = '';
                alert("Bien connecté!"); // Ou rediriger vers une autre page
                window.location.href = "home.jsp";
                //window.location.href = "profile.jsp";
                <!-- <iframe src="login.jsp" frameborder="0" style="width:100%; height:100vh"></iframe> -->
            })
            .catch(error => {
                document.getElementById('errorMessage').textContent = "Le login ou le mot de passe est incorrect!";
            });
        });
    </script>
</body>
</html>