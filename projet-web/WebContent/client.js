let titrepage = document.createElement("H1");
titrepage.textContent = "Hello, welcome!";
document.body.appendChild(titrepage);

let login = document.createElement("FORM");
login.setAttribute("action", "");
login.setAttribute("method", "get");
login.setAttribute("class", "login");

let ligne1 = document.createElement("DIV");
login.appendChild(ligne1);
let lab1 = document.createElement("LABEL");
lab1.setAttribute("for", "username");
lab1.textContent = "login:"
let inp1 = document.createElement("INPUT");
inp1.setAttribute("type", "text");
inp1.setAttribute("name", "username");
inp1.setAttribute("id", "username");
ligne1.appendChild(lab1);
ligne1.appendChild(inp1);

let ligne2 = document.createElement("DIV");
login.appendChild(ligne2);
let lab2 = document.createElement("LABEL");
lab2.setAttribute("for", "password");
lab2.textContent = "password:"
let inp2 = document.createElement("INPUT");
inp2.setAttribute("type", "text");
inp2.setAttribute("name", "password");
inp2.setAttribute("id", "password");
ligne2.appendChild(lab2);
ligne2.appendChild(inp2);

let ligne3 = document.createElement("DIV");
login.appendChild(ligne3);
let inp3 = document.createElement("INPUT");
inp3.setAttribute("type", "submit");
inp3.setAttribute("value", "login in");
ligne3.appendChild(inp3);

let ligne4 = document.createElement("DIV");
login.appendChild(ligne4);
let lab4 = document.createElement("LABEL");
lab4.setAttribute("for", "password");
lab4.textContent = "Don't have a account?"
let inp4 = document.createElement("INPUT");
inp4.setAttribute("type", "submit");
inp4.setAttribute("value", "Sign Up");
ligne4.appendChild(lab4);
ligne4.appendChild(inp4);

document.body.appendChild(login);

login.addEventListener("submit", function(event){
	event.preventDefault(); 
	console.log("Submit log-in"); 
	auth(event)
	});

function auth(event){
	let url = 'user?username='+inp1.value;
	console.log("fetch a "+url);
	let headers = new Headers();
	headers.set('Accept', 'application/json');
	fetch(url, {method : "get", headers, mode : "same-origin"})
		.then(function(response) {
			return response.json();
		})
		.then(function(data){
			console.log("Recu:" + data);
			entree(data)
		})
		.catch(function(error){console.log("Authentification Parsing: "+error.message)});
}

function entree(data){
	let authElement = document.getElementById('auth');
    if (authElement) {
        document.body.removeChild(authElement);
    }
	if (data.password == inp2.value) {
		console.log("Recu:" + data.resultat);
		accueil(data.username)
	}
	else {
		document.body.innerHTML += "<h2 color=red> Mauvais mot de passe ou login inexistant ! </h2>"
	}
	let bout = document.createElement("BUTTON")
	bout.textContent = "Reessayer";
	document.body.appendChild(bout);
	bout.addEventListener("click", function(_){console.log("Reessayer");document.location.reload();}) 
}

// =========================================

function recupereUtil(login, cont){
	let xhr = new XMLHttpRequest();
	xhr.open('GET', 'util?login=' + login);
	xhr.send();
	xhr.addEventListener("readystatechange", function(event){	
		if (xhr.readyState === XMLHttpRequest.DONE) {
				let jresp = JSON.parse(xhr.response)
				cont(jresp)
				}
			})
}

function accueil(login){
	document.body.innerHTML = "";
	recupereUtil(login, accueil2);
}