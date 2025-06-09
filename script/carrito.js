let mesaUUID = sessionStorage.getItem("mesaUUID");

function obtenerNumeroMesa(mesaUUID){
  fetch(`https://bugbustersspring.onrender.com/api/sesiones/${mesaUUID}/numero-mesa`)
  .then(res=> res.json())
  .then(id=>{
    const mesa= document.getElementById("numero-mesa");
    mesa.textContent = `Mesa ${id}`;
  })
}

function enviarActualizacionAlBackend(productoId, cantidad) {
  const pedidoId = localStorage.getItem(`pedido_mesa_${mesaUUID}`);
  if (!pedidoId) return;

  fetch(`https://bugbustersspring.onrender.com/api/pedidos/${pedidoId}/items`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ productoId, cantidad })
  })
  .then(response => {
    if (!response.ok) {
      console.error("Error al actualizar el pedido");
    }
  })
  .catch(err => console.error("Error de red:", err));
}

const contenedor = document.getElementById("carrito-contenedor");

function actualizarContadorCarrito() {
  const carrito = obtenerCarrito();
  const total = carrito.reduce((sum, item) => sum + item.cantidad, 0);
  const spanContador = document.getElementById("contador-carrito");
  if (spanContador) {
    spanContador.textContent = total;
  }
}

function actualizarPrecioTotal() {
  const carrito = obtenerCarrito();
  const total = carrito.reduce((sum, item) => sum + item.precio * item.cantidad, 0);
  const totalElement = document.getElementById("precio-total");
  if (totalElement) {
    totalElement.textContent = total.toFixed(2).replace(".", ",") + "€";
  }
}

function obtenerCarrito() {
  return JSON.parse(localStorage.getItem(`carrito_${mesaUUID}`)) || [];
}

function guardarCarrito(carrito) {
  localStorage.setItem(`carrito_${mesaUUID}`, JSON.stringify(carrito));
}

function añadirAlCarrito(producto) {
  const carrito = obtenerCarrito();
  const existente = carrito.find(item => item.id === producto.id);
  let nuevaCantidad;

  if (existente) {
    existente.cantidad += 1;
    nuevaCantidad = existente.cantidad;
  } else {
    carrito.push({ ...producto, cantidad: 1 });
    nuevaCantidad = 1;
  }

  guardarCarrito(carrito);
  actualizarContadorCarrito();
  actualizarPrecioTotal();
  enviarActualizacionAlBackend(producto.id, nuevaCantidad);
}

function eliminarDelCarrito(producto) {
  const carrito = obtenerCarrito();
  const index = carrito.findIndex(item => item.id === producto.id);
  if (index !== -1) {
    carrito[index].cantidad -= 1;
    const cantidadActual = carrito[index].cantidad;

    if (cantidadActual <= 0) {
      carrito.splice(index, 1);
    }

    guardarCarrito(carrito);
    actualizarContadorCarrito();
    actualizarPrecioTotal();
    enviarActualizacionAlBackend(producto.id, Math.max(0, cantidadActual));
  }
}

document.getElementById("confirmar-compra").addEventListener("click", () => {
  const carrito = obtenerCarrito();
  if (carrito.length === 0) {
    alert("No hay productos en el carrito.");
    return;
  }

  const pedido = {
    sesionId: mesaUUID,
    trabajadorId: 1,
    estado: "PENDIENTE",
    total: carrito.reduce((sum, item) => sum + item.precio * item.cantidad, 0),
    notas: "",
    fechaCreacion: new Date(),
    items: carrito.map(item => ({
      productoId: item.id,
      cantidad: item.cantidad,
      precioUnitario: item.precio,
      estado: "PENDIENTE",
      notas: item.notas || ""
    }))
  };

  fetch("https://bugbustersspring.onrender.com/api/pedidos", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(pedido)
  })
  .then(res => {
    if (!res.ok) throw new Error("Error al guardar el pedido");
    return res.text();
  })
  .then(data => {
    console.log("Pedido guardado:", data);
    const historial = JSON.parse(localStorage.getItem(`historial_${mesaUUID}`)) || [];
    historial.push({ fechaHora: new Date().toISOString(), items: carrito });
    localStorage.setItem(`historial_${mesaUUID}`, JSON.stringify(historial));
    localStorage.removeItem(`carrito_${mesaUUID}`);
    window.location.href = "historial.html";
  })
  .catch(err => {
    console.error("Error:", err);
    alert("Hubo un error al enviar el pedido.");
  });
});

const carrito = obtenerCarrito();
carrito.forEach(producto => {
  const divPlato = document.createElement("div");
  divPlato.className = "plato";
  divPlato.setAttribute("data-producto-id", producto.id);
  divPlato.innerHTML = `
    <div class="info-plato">
      <span class="contador">${producto.cantidad}</span>
      <div class="separador"></div>
      <div class="texto-plato">
        <div class="nombre-plato">${producto.nombre}</div>
        <div class="precio">${producto.precio.toFixed(2)}€</div>
        <button class="btn-notas" title="Añadir notas">📝</button>
      </div>
      <img class="imagen-plato" src="${producto.imagenes}" alt="${producto.nombre}">
    </div>
    <div class="control-cantidad">
      <button class="btn-menos">−</button>
      <button class="btn-mas">+</button>
    </div>
    <textarea class="notas-textarea" placeholder="Escribe una nota..." style="display:none"></textarea>
  `;

  const contadorSpan = divPlato.querySelector(".contador");
  const btnMas = divPlato.querySelector(".btn-mas");
  const btnMenos = divPlato.querySelector(".btn-menos");
  const btnNotas = divPlato.querySelector(".btn-notas");
  const textareaNotas = divPlato.querySelector(".notas-textarea");

  const itemEnCarrito = carrito.find(item => item.id === producto.id);
  if (itemEnCarrito && itemEnCarrito.notas) {
    textareaNotas.value = itemEnCarrito.notas;
  }

  btnNotas.addEventListener("click", () => {
    textareaNotas.style.display = textareaNotas.style.display === "none" ? "block" : "none";
  });
  
  textareaNotas.addEventListener("input", () => {
    const carritoActual = obtenerCarrito();
    const item = carritoActual.find(i => i.id === producto.id);
    if (item) {
      item.notas = textareaNotas.value.trim();
      guardarCarrito(carritoActual);
    }
  });

  btnMas.addEventListener("click", () => {
    añadirAlCarrito(producto);
    contadorSpan.textContent = parseInt(contadorSpan.textContent) + 1;
  });

  btnMenos.addEventListener("click", () => {
    eliminarDelCarrito(producto);
    let cantidadActual = parseInt(contadorSpan.textContent);
    if (cantidadActual > 1) {
      cantidadActual -= 1;
      contadorSpan.textContent = cantidadActual;
    } else {
      divPlato.remove();
    }
  });

  contenedor.appendChild(divPlato);
});

const textareas = document.querySelectorAll('textarea');

textareas.forEach(textarea => {
  textarea.addEventListener('input', () => {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  });
});

document.addEventListener("DOMContentLoaded", () => {
  actualizarContadorCarrito();
  actualizarPrecioTotal();
  obtenerNumeroMesa(mesaUUID);
});
