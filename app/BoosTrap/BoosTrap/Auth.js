document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");

    // Obtener usuarios almacenados o inicializar lista vacía
    let users = JSON.parse(localStorage.getItem("users")) || [];

    // Verificar autenticación antes de acceder al dashboard
    function checkAuth() {
        const user = JSON.parse(localStorage.getItem("loggedInUser"));
        if (!user && window.location.pathname.includes("Dashboard.html")) {
            alert("Debes iniciar sesión primero.");
            window.location.href = "Inicio-Sesion.html";
        }
    }
    checkAuth();

    // Función para validar contraseña (mínimo 6 caracteres, al menos una letra y un número)
    function validarPassword(password) {
        return password.length >= 6 && /\d/.test(password) && /[A-Za-z]/.test(password);
    }

    // Registro de nuevos usuarios
    if (registerForm) {
        registerForm.addEventListener("submit", function (event) {
            event.preventDefault(); // Evita recargar la página

            // Obtener datos del formulario
            const name = document.getElementById("name").value.trim();
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value.trim();

            // Validaciones
            if (!name || !email || !password) {
                alert("Todos los campos son obligatorios.");
                return;
            }

            if (!validarPassword(password)) {
                alert("La contraseña debe tener al menos 6 caracteres, incluyendo una letra y un número.");
                return;
            }

            // Verificar si el correo ya está registrado
            if (users.some(user => user.email === email)) {
                alert("Este correo ya está registrado.");
                return;
            }

            // Agregar nuevo usuario y guardar en localStorage
            users.push({ name, email, password });
            localStorage.setItem("users", JSON.stringify(users));

            alert("Registro exitoso. Ahora puedes iniciar sesión.");
            window.location.href = "Inicio-Sesion.html"; // Redirigir al login
        });
    }

    // Inicio de sesión
    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault(); // Evita recarga de la página

            // Obtener datos ingresados
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value.trim();

            // Buscar usuario en la lista
            const user = users.find(user => user.email === email && user.password === password);

            if (user) {
                localStorage.setItem("loggedInUser", JSON.stringify(user));
                window.location.href = "Dashboard.html"; // Redirigir al dashboard
            } else {
                alert("Credenciales incorrectas. Inténtalo de nuevo.");
            }
        });
    }

    // Cerrar sesión
    const logoutButton = document.getElementById("logout");
    if (logoutButton) {
        logoutButton.addEventListener("click", function () {
            localStorage.removeItem("loggedInUser");
            window.location.href = "Inicio-Sesion.html";
        });
    }
});
