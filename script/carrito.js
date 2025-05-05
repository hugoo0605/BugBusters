const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
const contenedor = document.getElementById("carrito-contenedor");

carrito.forEach(producto => {
  const plato = document.createElement("div");
  plato.className = "plato";
  plato.innerHTML = `
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
  contenedor.appendChild(plato);
});