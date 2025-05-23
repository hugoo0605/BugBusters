const params = new URLSearchParams(window.location.search);
const mesaUUID = params.get("mesa");

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

const contenedor = document.getElementById("carrito-contenedor");

function actualizarContadorCarrito() {
  const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
  const total = carrito.reduce((sum, item) => sum + item.cantidad, 0);
  const spanContador = document.getElementById("contador-carrito");
  if (spanContador) {
    spanContador.textContent = total;
  }
}

function actualizarPrecioTotal() {
  const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
  const total = carrito.reduce((sum, item) => sum + item.precio * item.cantidad, 0);
  const totalElement = document.getElementById("precio-total");
  if (totalElement) {
    totalElement.textContent = total.toFixed(2).replace(".", ",") + "€";
  }
}

function obtenerCarrito() {
  return JSON.parse(localStorage.getItem("carrito")) || [];
}

function guardarCarrito(carrito) {
  localStorage.setItem("carrito", JSON.stringify(carrito));
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
  actualizarPrecioTotal();
  enviarActualizacionAlBackend(producto.id, existente ? existente.cantidad : 1, mesaUUID);
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
    enviarActualizacionAlBackend(producto.id, cantidadActual > 0 ? cantidadActual : 0, mesaUUID);
  }
}

document.getElementById("confirmar-compra").addEventListener("click", () => {
  const carrito = obtenerCarrito();
  if (carrito.length === 0) {
    alert("No hay productos en el carrito.");
    return;
  }

  const nuevoPedido = {
    fechaHora: new Date().toISOString(),
    items: carrito
  };

  const pedido = {
    sesionId: mesaUUID, // Mesa real obtenida por URL
    trabajadorId: 1,
    estado: "PENDIENTE",
    total: carrito.reduce((sum, item) => sum + item.precio * item.cantidad, 0),
    notas: "",
    items: carrito.map(item => ({
      productoId: item.id,
      cantidad: item.cantidad,
      precioUnitario: item.precio,
      estado: "PENDIENTE",
      notas: ""
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
      const historial = JSON.parse(localStorage.getItem("historial")) || [];
      historial.push(nuevoPedido);
      localStorage.setItem("historial", JSON.stringify(historial));
      localStorage.removeItem("carrito");
      window.location.href = "historial.html";
    })
    .catch(err => {
      console.error("Error:", err);
      alert("Hubo un error al enviar el pedido.");
    });
});

// Mostrar productos en el carrito
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
      </div>
      <img class="imagen-plato" src="${producto.imagenes}" alt="${producto.nombre}">
    </div>
    <div class="control-cantidad">
      <button class="btn-menos">−</button>
      <button class="btn-mas">+</button>
    </div>
  `;

  const contadorSpan = divPlato.querySelector(".contador");
  const btnMas = divPlato.querySelector(".btn-mas");
  const btnMenos = divPlato.querySelector(".btn-menos");

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

document.addEventListener("DOMContentLoaded", () => {
  actualizarContadorCarrito();
  actualizarPrecioTotal();
});
