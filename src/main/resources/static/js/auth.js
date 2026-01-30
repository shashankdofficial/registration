const API_URL = "/api/auth";

// ======================
// SIGNUP
// ======================
function signup() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch(API_URL + "/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    })
    .then(res => {
        if (!res.ok) throw new Error("Signup failed");
        return res.json();
    })
    .then(() => {
        alert("Signup successful! Please login.");
        window.location.href = "login.html";
    })
    .catch(err => alert(err.message));
}

// ======================
// LOGIN
// ======================
// function login() {
//     const username = document.getElementById("username").value;
//     const password = document.getElementById("password").value;

//     fetch(API_URL + "/login", {
//         method: "POST",
//         headers: {
//             "Content-Type": "application/json"
//         },
//         body: JSON.stringify({ username, password })
//     })
//     .then(res => {
//         if (!res.ok) throw new Error("Invalid credentials");
//         return res.json();
//     })
//     .then(data => {
//         localStorage.setItem("token", data.token);
//         window.location.href = "dashboard.html";
//     })
//     .catch(err => alert(err.message));
// }

function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch(API_URL + "/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include", // ⭐ IMPORTANT
        body: JSON.stringify({ username, password })
    })
    .then(res => {
        if (!res.ok) throw new Error("Invalid credentials");
        window.location.href = "dashboard.html";
    })
    .catch(err => alert(err.message));
}

// ======================
// CHECK AUTH
// ======================
// function checkAuth() {
//     const token = localStorage.getItem("token");

//     if (!token) {
//         window.location.href = "login.html";
//     }
// }

// function loadUser() {
//     const token = localStorage.getItem("token");

//     fetch("http://localhost:8080/api/user/me", {
//         headers: {
//             "Authorization": "Bearer " + token
//         }
//     })
//     .then(res => {
//         if (!res.ok) throw new Error("Unauthorized");
//         return res.json();
//     })
//     .then(data => {
//         document.getElementById("welcomeText")
//             .innerText = "Welcome " + data.username;
//     })
//     .catch(() => {
//         localStorage.removeItem("token");
//         window.location.href = "login.html";
//     });
// }

function loadUser() {
    fetch("/api/user/me", {
        credentials: "include"
    })
    .then(res => {
        if (!res.ok) throw new Error("Unauthorized");
        return res.json();
    })
    .then(data => {
        document.getElementById("welcomeText")
            .innerText = "Welcome " + data.username;
    })
    .catch(() => {
        window.location.href = "login.html";
    });
}


// ======================
// LOGOUT
// ======================
// function logout() {
//     localStorage.removeItem("token");
//     window.location.href = "login.html";
// }

function logout() {
    fetch("/api/auth/logout", {
        method: "POST",
        credentials: "include"
    })
    .then(() => window.location.href = "login.html");
}

