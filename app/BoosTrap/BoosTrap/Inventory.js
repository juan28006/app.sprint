document.addEventListener("DOMContentLoaded", function () {
    // Referencias a elementos del DOM
    const inventoryTable = document.querySelector("#inventoryTable"); 
    const addProductForm = document.querySelector("#addProductForm"); 
    const addProductModalElement = document.querySelector("#addProductModal"); 
    const addProductModal = new bootstrap.Modal(addProductModalElement); 

    let products = JSON.parse(localStorage.getItem("inventory")) || []; 
    let editIndex = -1; // Índice del producto en edición (-1 significa nuevo producto)

    // Función para mostrar los productos en la tabla
    function renderTable() {
        inventoryTable.innerHTML = ""; // Limpiar tabla antes de actualizar

        products.forEach((product, index) => {
            let row = document.createElement("tr");
            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${product.category}</td>
                <td>${product.quantity}</td>
                <td><span class="badge bg-${getStatusColor(product.status)}">${product.status}</span></td>
                <td>
                    <button class="btn btn-warning btn-sm" onclick="editProduct(${index})">✏️ Editar</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteProduct(${index})">🗑 Eliminar</button>
                </td>
            `;
            inventoryTable.appendChild(row);
        });
    }

    // Evento para agregar o actualizar producto
    addProductForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Evitar recarga de la página

        // Obtener valores del formulario
        const category = document.querySelector("#productCategory").value;
        const quantity = document.querySelector("#productQuantity").value;
        const status = document.querySelector("#productStatus").value;

        // Verificar si estamos agregando o editando un producto
        if (editIndex === -1) {
            products.push({ category, quantity, status });
        } else {
            products[editIndex] = { category, quantity, status };
            editIndex = -1; // Resetear el índice de edición
        }

        // Guardar en localStorage
        localStorage.setItem("inventory", JSON.stringify(products));

        // Actualizar la tabla y cerrar el modal
        renderTable();
        addProductForm.reset();
        addProductModal.hide();
    });

    // Función para editar un producto
    window.editProduct = function (index) {
        let product = products[index];

        // Cargar valores del producto en el formulario
        document.querySelector("#productCategory").value = product.category;
        document.querySelector("#productQuantity").value = product.quantity;
        document.querySelector("#productStatus").value = product.status;

        editIndex = index; // Guardar el índice del producto en edición
        addProductModal.show(); // Mostrar el modal
    };

    // Función para eliminar un producto
    window.deleteProduct = function (index) {
        let confirmDelete = confirm("¿Seguro que deseas eliminar este producto?");
        if (confirmDelete) {
            products.splice(index, 1);
            localStorage.setItem("inventory", JSON.stringify(products));
            renderTable();
        }
    };

    // Función para determinar el color de estado
    function getStatusColor(status) {
        if (status === "Disponible") return "success";
        if (status === "En reparación") return "warning";
        return "danger";
    }
    // Cerrar sesión
    const logoutButton = document.getElementById("logout");
    if (logoutButton) {
        logoutButton.addEventListener("click", function () {
            localStorage.removeItem("loggedInUser");
            window.location.href = "Registro.html";
        });
    }

    // Resetear formulario cuando se cierra el modal
    addProductModalElement.addEventListener("hidden.bs.modal", function () {
        editIndex = -1;
        addProductForm.reset();
    });

    // Mostrar la tabla con productos al cargar la página
    renderTable();
});

// Reservar maquinaria (simulado con prompt + alerta)
document.getElementById("btnReservar").addEventListener("click", function () {
    if (products.length === 0) {
        alert("No hay maquinaria disponible para reservar.");
        return;
    }

    let maquinariaDisponible = products
        .map((p, i) => `${i + 1}. ${p.category} (${p.status})`)
        .join("\n");

    let seleccion = prompt(
        "Selecciona el número de la maquinaria a reservar:\n" + maquinariaDisponible
    );

    let index = parseInt(seleccion) - 1;
    if (!isNaN(index) && products[index]) {
        alert(`Reservaste: ${products[index].category}`);
    } else {
        alert("Selección inválida.");
    }
});

// Enviar notificación sobre nuevas máquinas (simulado)
document.getElementById("btnNotificar").addEventListener("click", function () {
    alert("✅ Notificación enviada a todos los usuarios:\n\n" +
        "¡Nuevas máquinas han sido ingresadas al inventario!\n" +
        "Ubicación: Zona de fuerza.\n" +
        "Por favor, acérquese a recepción para más detalles.");
});

// Generar informe de maquinaria (simulado)
document.getElementById("btnInforme").addEventListener("click", function () {
    if (products.length === 0) {
        alert("No hay datos en el inventario para generar un informe.");
        return;
    }

    let informe = products
        .map((p, i) =>
            `${i + 1}. ${p.category} | Cantidad: ${p.quantity} | Estado: ${p.status}`
        )
        .join("\n");

    alert("📄 Informe de maquinaria:\n\n" + informe);
});

// Redirigir a orden de compra
document.getElementById("btnOrdenCompra").addEventListener("click", function () {
    window.open(
        "https://ultimatefitness.com.co/?gad_source=1&gclid=Cj0KCQjwqv2_BhC0ARIsAFb5Ac_LHicEj21NrvyqKqXf5riPNBpWvH0_sh8rPCfw9I5SFtAhEIY76i0aAiqQEALw_wcB",
        "_blank"
    );
});