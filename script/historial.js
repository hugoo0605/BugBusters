//Obtiene el numero de la mesa
function obtenerNumeroMesa(mesaUUID){
  fetch(`https://bugbustersspring.onrender.com/api/sesiones/${mesaUUID}/numero-mesa`)
  .then(res=> res.json())
  .then(id=>{
    const mesa= document.getElementById("numero-mesa");
    mesa.textContent = `Mesa ${id}`;
  })
}

//Actualiza el contador del carrito
function actualizarContadorCarrito() {
  const mesaUUID = sessionStorage.getItem("mesaUUID");
  const carrito = JSON.parse(localStorage.getItem(`carrito_${mesaUUID}`)) || [];
  const total = carrito.reduce((sum, item) => sum + item.cantidad, 0);
  const spanContador = document.getElementById("contador-carrito");
  if (spanContador) {
    spanContador.textContent = total;
  }
}

//Carga los pedidos del historial desde el localstorage
document.addEventListener("DOMContentLoaded", () => {
    actualizarContadorCarrito();
    obtenerNumeroMesa(mesaUUID);
});
const mesaUUID = sessionStorage.getItem("mesaUUID");
const historial = JSON.parse(localStorage.getItem(`historial_${mesaUUID}`)) || [];
const contenedor = document.getElementById("contenedor-historial");

historial.forEach(pedido => {
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

