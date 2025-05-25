let mesaUUID = obtenerMesaDesdeURLoSession();

function obtenerMesaDesdeURLoSession() {
  const params = new URLSearchParams(window.location.search);
  const mesaUUIDEnURL = params.get('mesa');

  if (mesaUUIDEnURL) {
    sessionStorage.setItem('mesaUUID', mesaUUIDEnURL);
    return mesaUUIDEnURL;
  }

  return sessionStorage.getItem('mesaUUID');
}

function obtenerNumeroMesa(mesaUUID){
  fetch(`https://bugbustersspring.onrender.com/api/sesiones/${mesaUUID}/mesa-id`)
  .then(res=> res.json())
  .then(id=>{
    const mesa= document.getElementById("numero-mesa");
    mesa.textContent = `Mesa ${id}`;
  })
}


function actualizarUIConCarrito(carrito) {
  carrito.forEach(item => {
    const platoDiv = document.querySelector(`.plato[data-producto-id="${item.id}"]`);
    if (platoDiv) {
      const contador = platoDiv.querySelector(".contador");
      if (contador) {
        contador.textContent = item.cantidad;
      }
    }
  });
}

function actualizarInterfazConPedido(pedido) {
  if (!pedido || !pedido.items) return;

  pedido.items.forEach(item => {
    const platoDiv = document.querySelector(`.plato[data-producto-id="${item.producto.id}"]`);
    if (platoDiv) {
    const contador = platoDiv.querySelector(".contador");
    if (contador) {
      contador.textContent = item.cantidad;
    }
    }
  });
}

function obtenerCarrito() {
  const mesaUUID = sessionStorage.getItem("mesaUUID");
  return JSON.parse(localStorage.getItem(`carrito_${mesaUUID}`)) || [];
}

function guardarCarrito(carrito) {
  const mesaUUID = sessionStorage.getItem("mesaUUID");
  localStorage.setItem(`carrito_${mesaUUID}`, JSON.stringify(carrito));
}

function actualizarContadorCarrito() {
  const mesaUUID = sessionStorage.getItem("mesaUUID");
  const carrito = JSON.parse(localStorage.getItem(`carrito_${mesaUUID}`)) || [];
  const total = carrito.reduce((sum, item) => sum + item.cantidad, 0);
  const spanContador = document.getElementById("contador-carrito");
  if (spanContador) {
    spanContador.textContent = total;
  }
}

function añadirAlCarrito(producto) {
  const carrito = obtenerCarrito();
  const existente = carrito.find(item => item.id === producto.id);
  if (existente) {
    existente.cantidad += 1;
  } else {
    carrito.push({ ...producto, cantidad: 1 });
  }
  guardarCarrito(carrito);
  actualizarContadorCarrito();
}

function eliminarDelCarrito(producto) {
  const carrito = obtenerCarrito();
  const index = carrito.findIndex(item => item.id === producto.id);
  if (index !== -1) {
    carrito[index].cantidad -= 1;
    if (carrito[index].cantidad <= 0) {
      carrito.splice(index, 1);
    }
    guardarCarrito(carrito);
    actualizarContadorCarrito();
  }
}

function mostrarProductos(categoriaSeleccionada = "TODOS") {
  fetch("https://bugbustersspring.onrender.com/api/productos")
    .then(res => res.json())
    .then(productos => {
      const contenedor = document.getElementById("contenedor-menu");
      contenedor.innerHTML = "";

      const filtrados = categoriaSeleccionada === "TODOS"
        ? productos
        : productos.filter(p => p.categoria === categoriaSeleccionada);

      const carrito = obtenerCarrito();

      filtrados.forEach(producto => {
        const enCarrito = carrito.find(item => item.id === producto.id);
        const cantidad = enCarrito ? enCarrito.cantidad : 0;

        const divPlato = document.createElement("div");
        divPlato.className = "plato";
        divPlato.setAttribute("data-producto-id", producto.id);
        divPlato.innerHTML = `
          <div class="info-plato">
              <div class="texto-plato">
                  <div class="nombre-plato">${producto.nombre}</div>
                  <div class="precio">${producto.precio.toFixed(2).replace(".", ",")}€</div>
              </div>
              <img class="imagen-plato" src="${producto.imagenes}" alt="${producto.nombre}">
          </div>
          <div class="control-cantidad">
              <button class="btn-menos">−</button>
              <span class="contador">${cantidad}</span>
              <button class="btn-mas">+</button>
          </div>
        `;

        const btnMas = divPlato.querySelector(".btn-mas");
        const btnMenos = divPlato.querySelector(".btn-menos");
        const contadorSpan = divPlato.querySelector(".contador");

        btnMas.addEventListener("click", () => {
          let nuevaCantidad = parseInt(contadorSpan.textContent, 10) + 1;
          contadorSpan.textContent = nuevaCantidad;
          añadirAlCarrito(producto);
          enviarActualizacionAlBackend(producto.id, nuevaCantidad, mesaUUID);
        });

        btnMenos.addEventListener("click", () => {
          let actual = parseInt(contadorSpan.textContent, 10);
          if (actual > 0) {
            contadorSpan.textContent = actual - 1;
            eliminarDelCarrito(producto);
            enviarActualizacionAlBackend(producto.id, nuevaCantidad, mesaUUID);
          }
        });

        const infoPlato = divPlato.querySelector(".info-plato");

        infoPlato.addEventListener('click', () => {
          modalImg.src = producto.imagenes;
          modalImg.alt = producto.nombre;
          modalName.textContent = producto.nombre;
          modalDescription.textContent = producto.descripcion;
          modal.classList.add('active');
        });

        contenedor.appendChild(divPlato);
      });
    });
}

// Modal
const modal = document.getElementById('modal');
const modalImg = document.getElementById('modal-img');
const modalName = document.getElementById('modal-name');
const modalDescription = document.getElementById('modal-description');
const closeBtn = document.getElementById('close-modal');

closeBtn.addEventListener('click', () => {
  modal.classList.remove('active');
});
modal.addEventListener('click', (e) => {
  if (e.target === modal) {
    modal.classList.remove('active');
  }
});

// Mostrar categorías y manejar clics
fetch("https://bugbustersspring.onrender.com/api/productos/categorias")
  .then(res => res.json())
  .then(categorias => {
    const contenedorCategorias = document.querySelector(".contenedor-categorias");
    contenedorCategorias.innerHTML = "";

    const botonTodos = document.createElement("div");
    botonTodos.classList.add("categoria");
    botonTodos.textContent = "TODOS";
    botonTodos.dataset.categoria = "TODOS";
    contenedorCategorias.appendChild(botonTodos);

    categorias.forEach(categoria => {
      const div = document.createElement("div");
      div.classList.add("categoria");
      div.textContent = categoria;
      div.dataset.categoria = categoria;
      contenedorCategorias.appendChild(div);
    });

    document.querySelectorAll(".categoria").forEach(btn => {
      btn.addEventListener("click", () => {
        mostrarProductos(btn.dataset.categoria);
      });
    });
  });

// Mostrar productos al cargar y al volver desde historial
document.addEventListener("DOMContentLoaded", () => {
  mostrarProductos("TODOS");
  actualizarContadorCarrito();

  const socket = new SockJS("https://bugbustersspring.onrender.com/ws");
  const stompClient = Stomp.over(socket);

  stompClient.connect({}, function () {
    mesaUUID = obtenerMesaDesdeURLoSession();

    console.log(`Suscrito al canal /topic/mesa/${mesaUUID}`);

    stompClient.subscribe(`/topic/mesa/${mesaUUID}`, function (mensaje) {
    const pedido = JSON.parse(mensaje.body);

    const nuevoCarrito = pedido.items.map(item => ({
      id: item.producto.id,
      nombre: item.producto.nombre,
      precio: item.producto.precio,
      imagenes: item.producto.imagenes,
      cantidad: item.cantidad
    }));

    localStorage.setItem(`carrito_${mesaUUID}`, JSON.stringify(nuevoCarrito));
    actualizarUIConCarrito(nuevoCarrito);
    actualizarContadorCarrito();
    actualizarPrecioTotal();
    obtenerNumeroMesa(mesaUUID);
  });
  });
});


// Si vuelves a esta página desde el historial (botón atrás del navegador)
window.addEventListener("pageshow", (event) => {
  if (event.persisted || performance.getEntriesByType("navigation")[0].type === "back_forward") {
    mostrarProductos("TODOS");
  }
});

function enviarActualizacionAlBackend(productoId, cantidad, mesaUUID) {
  const pedidoId = localStorage.getItem(`pedido_mesa_${mesaUUID}`);
  if (!pedidoId) return;

  fetch(`https://bugbustersspring.onrender.com/api/pedidos/${pedidoId}/items`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      productoId: productoId,
      cantidad: cantidad
    })
  })
  .then(response => {
  if (!response.ok) {
  console.error("Error al actualizar el pedido");
  }
  })
  .catch(err => console.error("Error de red:", err));
}
