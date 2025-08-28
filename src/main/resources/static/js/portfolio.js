function openModal(title, description, image) {
  document.getElementById("modalTitle").innerText = title;
  document.getElementById("modalDescription").innerText = description;
  document.getElementById("modalImage").src = image;
  document.getElementById("portfolioModal").style.display = "block";
}

function closeModal() {
  document.getElementById("portfolioModal").style.display = "none";
}
