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
