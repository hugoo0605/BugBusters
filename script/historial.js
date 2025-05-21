function actualizarContadorCarrito() {
    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    const total = carrito.reduce((sum, item) => sum + item.cantidad, 0);
    const spanContador = document.getElementById("contador-carrito");
    if (spanContador) {
      spanContador.textContent = total;
    }
}

document.addEventListener("DOMContentLoaded", () => {
    actualizarContadorCarrito();
});
const historial = JSON.parse(localStorage.getItem("historial")) || [];
const contenedor = document.getElementById("contenedor-historial");

historial.forEach(pedido => {
  // Mostrar la fecha y hora del pedido
  const fechaPedido = new Date(pedido.fechaHora);

  const divFecha = document.createElement("div");
  divFecha.className = "fecha-pedido";
  divFecha.textContent = `Pedido realizado el ${fechaPedido.toLocaleDateString()} a las ${fechaPedido.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
  contenedor.appendChild(divFecha);

  pedido.items.forEach(producto => {
    const divPlato = document.createElement("div");
    divPlato.className = "plato";
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
    `;
    contenedor.appendChild(divPlato);
  });
});

