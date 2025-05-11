// Acceso global a los productos
let products = JSON.parse(localStorage.getItem("inventory")) || [];

document.addEventListener("DOMContentLoaded", function () {
    const inventoryTable = document.querySelector("#inventoryTable");
    const addProductForm = document.querySelector("#addProductForm");
    const addProductModalElement = document.querySelector("#addProductModal");
    
    let addProductModal = null;
    if (addProductModalElement) {
        addProductModal = new bootstrap.Modal(addProductModalElement);
    }

    const agregarProductoBtn = document.getElementById("agregarProductoBtn");
    let editIndex = -1;

    if (inventoryTable) renderTable();

    if (agregarProductoBtn && addProductModal) {
        agregarProductoBtn.addEventListener("click", () => {
            addProductForm.reset();
            editIndex = -1;
            addProductModal.show();
        });
    }

    if (addProductForm) {
        addProductForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const category = document.querySelector("#productCategory").value;
            const quantity = document.querySelector("#productQuantity").value;
            const status = document.querySelector("#productStatus").value;

            if (editIndex === -1) {
                products.push({ category, quantity, status });
            } else {
                products[editIndex] = { category, quantity, status };
                editIndex = -1;
            }

            localStorage.setItem("inventory", JSON.stringify(products));
            if (inventoryTable) renderTable();
            addProductForm.reset();
            if (addProductModal) addProductModal.hide();

            if (document.getElementById('maquinariaTable')) {
                cargarMaquinaria();
            }
        });
    }

    function renderTable() {
        if (!inventoryTable) return;

        inventoryTable.innerHTML = "";
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

    window.editProduct = function (index) {
        if (!addProductModal) return;

        const product = products[index];
        document.querySelector("#productCategory").value = product.category;
        document.querySelector("#productQuantity").value = product.quantity;
        document.querySelector("#productStatus").value = product.status;

        editIndex = index;
        addProductModal.show();
    };

    window.deleteProduct = function (index) {
        if (confirm("¿Seguro que deseas eliminar este producto?")) {
            products.splice(index, 1);
            localStorage.setItem("inventory", JSON.stringify(products));
            if (inventoryTable) renderTable();
            if (document.getElementById('maquinariaTable')) {
                cargarMaquinaria();
            }
        }
    };

    if (addProductModalElement) {
        addProductModalElement.addEventListener("hidden.bs.modal", function () {
            editIndex = -1;
            if (addProductForm) addProductForm.reset();
        });
    }

    function getStatusColor(status) {
        if (status === "Disponible") return "success";
        if (status === "En reparación") return "warning";
        return "danger";
    }
    window.getStatusColor = getStatusColor;

    const btnInforme = document.getElementById("btnInforme");
    if (btnInforme) {
        btnInforme.addEventListener("click", function () {
            if (products.length === 0) {
                alert("No hay datos en el inventario para generar un informe.");
                return;
            }

            const informe = products.map((p, i) =>
                `${i + 1}. ${p.category} | Cantidad: ${p.quantity} | Estado: ${p.status}`
            ).join("\n");

            alert("📄 Informe de maquinaria:\n\n" + informe);
        });
    }

    const btnNotificar = document.getElementById("btnNotificar");
    if (btnNotificar) {
        btnNotificar.addEventListener("click", function () {
            alert("✅ Informe de facturación:\n\n" +
                "Total de productos en inventario: " + products.length + "\n" +
                "Valor estimado del inventario: $" + (products.length * 1500) + "\n" +
                "Ingresos por reservas: $" + calcularIngresosReservas());
        });
    }

    function calcularIngresosReservas() {
        const reservas = JSON.parse(localStorage.getItem('reservas')) || [];
        return reservas.length * 150;
    }

    const maquinariaTable = document.getElementById('maquinariaTable');
    const noMaquinaria = document.getElementById('noMaquinaria');
    const reservasTable = document.getElementById('reservasTable');
    const noReservas = document.getElementById('noReservas');
    const reservaModalElement = document.getElementById('reservaModal');
    const cancelarModalElement = document.getElementById('cancelarModal');
    const reservaForm = document.getElementById('reservaForm');

    let reservaModal = reservaModalElement ? new bootstrap.Modal(reservaModalElement) : null;
    let cancelarModal = cancelarModalElement ? new bootstrap.Modal(cancelarModalElement) : null;

    const fechaReserva = document.getElementById('fechaReserva');
    if (fechaReserva) {
        const hoy = new Date().toISOString().split('T')[0];
        fechaReserva.setAttribute('min', hoy);
    }

    function cargarMaquinaria() {
        if (!maquinariaTable || !noMaquinaria) return;

        maquinariaTable.innerHTML = "";
        if (products.length === 0) {
            noMaquinaria.classList.remove('d-none');
            return;
        }

        noMaquinaria.classList.add('d-none');

        products.forEach((product, index) => {
            const disponibilidad = product.status === 'Disponible'
                ? '<span class="badge bg-success">Disponible</span>'
                : '<span class="badge bg-secondary">No disponible</span>';

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${product.category}</td>
                <td><span class="badge bg-${getStatusColor(product.status)}">${product.status}</span></td>
                <td>${disponibilidad}</td>
                <td>
                    <button class="btn btn-sm btn-primary ${product.status !== 'Disponible' ? 'disabled' : ''}" 
                        onclick="reservarMaquinaria(${index})">
                        Reservar
                    </button>
                </td>
            `;
            maquinariaTable.appendChild(row);
        });
    }

    function cargarReservas() {
        if (!reservasTable || !noReservas) return;

        const reservas = JSON.parse(localStorage.getItem('reservas')) || [];

        reservasTable.innerHTML = "";
        if (reservas.length === 0) {
            noReservas.classList.remove('d-none');
            return;
        }

        noReservas.classList.add('d-none');

        reservas.sort((a, b) => new Date(a.fecha) - new Date(b.fecha)).forEach((reserva, index) => {
            const maquinaria = products[reserva.maquinariaId] || { category: 'Desconocida' };
            const hoy = new Date();
            hoy.setHours(0, 0, 0, 0);
            const fechaReserva = new Date(reserva.fecha);

            let estado = '';
            if (fechaReserva < hoy) {
                estado = '<span class="badge bg-secondary">Finalizada</span>';
            } else if (fechaReserva.toDateString() === hoy.toDateString()) {
                estado = '<span class="badge bg-primary">Hoy</span>';
            } else {
                estado = '<span class="badge bg-success">Activa</span>';
            }

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${maquinaria.category}</td>
                <td>${formatearFecha(reserva.fecha)}</td>
                <td>${reserva.hora}</td>
                <td>${estado}</td>
                <td>
                    <button class="btn btn-sm btn-danger" onclick="mostrarCancelarReserva(${index})">
                        Cancelar
                    </button>
                </td>
            `;
            reservasTable.appendChild(row);
        });
    }

    function formatearFecha(fechaStr) {
        const fecha = new Date(fechaStr);
        return fecha.toLocaleDateString('es-ES', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
    }

    window.reservarMaquinaria = function(index) {
        if (!reservaModal) return;

        const maquinaria = products[index];
        if (maquinaria && maquinaria.status === 'Disponible') {
            document.getElementById('maquinariaId').value = index;
            document.getElementById('maquinariaNombre').value = maquinaria.category;
            reservaModal.show();
        } else {
            alert('Esta maquinaria no está disponible para reservar.');
        }
    };

    window.mostrarCancelarReserva = function(index) {
        if (!cancelarModal) return;
        document.getElementById('cancelarReservaId').value = index;
        cancelarModal.show();
    };

    const confirmarCancelar = document.getElementById('confirmarCancelar');
    if (confirmarCancelar) {
        confirmarCancelar.addEventListener('click', function () {
            const index = document.getElementById('cancelarReservaId').value;
            const reservas = JSON.parse(localStorage.getItem('reservas')) || [];
            reservas.splice(index, 1);
            localStorage.setItem('reservas', JSON.stringify(reservas));
            cancelarModal.hide();
            cargarReservas();
        });
    }

    if (reservaForm) {
        reservaForm.addEventListener('submit', function (e) {
            e.preventDefault();

            const maquinariaId = document.getElementById('maquinariaId').value;
            const fecha = document.getElementById('fechaReserva').value;
            const hora = document.getElementById('horaReserva').value;

            if (!validarDisponibilidadHora(maquinariaId, fecha, hora)) {
                alert('La hora seleccionada ya está reservada para esta máquina.');
                return;
            }

            const reservas = JSON.parse(localStorage.getItem('reservas')) || [];
            reservas.push({ maquinariaId, fecha, hora, timestamp: new Date().getTime() });
            localStorage.setItem('reservas', JSON.stringify(reservas));

            reservaModal.hide();
            alert('¡Reserva realizada con éxito!');
            cargarMaquinaria();
            cargarReservas();
        });
    }

    function validarDisponibilidadHora(maquinariaId, fecha, hora) {
        const reservas = JSON.parse(localStorage.getItem('reservas')) || [];
        return !reservas.find(reserva => 
            reserva.maquinariaId === maquinariaId &&
            reserva.fecha === fecha &&
            reserva.hora === hora
        );
    }

    if (maquinariaTable && reservasTable) {
        cargarMaquinaria();
        cargarReservas();
    }

    window.addEventListener('storage', function (e) {
        if (e.key === 'inventory' || e.key === 'reservas') {
            if (inventoryTable) renderTable();
            if (maquinariaTable && reservasTable) {
                cargarMaquinaria();
                cargarReservas();
            }
        }
    });
});
