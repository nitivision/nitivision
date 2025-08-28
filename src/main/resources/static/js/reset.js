const resetSection = document.getElementById("resetSection");
const responseMsg = document.getElementById("responseMsg");

function sendOtp() {
  const email = document.getElementById("email").value;

  fetch("/send-otp", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email })
  })
  .then(res => res.json().then(data => ({ status: res.status, body: data })))
  .then(({ status, body }) => {
    responseMsg.innerText = body.message;
    if (status === 200) {
      responseMsg.classList.remove("text-danger");
      responseMsg.classList.add("text-success");
      resetSection.classList.remove("d-none");
    } else {
      responseMsg.classList.remove("text-success");
      responseMsg.classList.add("text-danger");
    }
  })
  .catch(() => {
    responseMsg.innerText = "Something went wrong.";
    responseMsg.classList.remove("text-success");
    responseMsg.classList.add("text-danger");
  });
}

function resetPassword() {
  const email = document.getElementById("email").value;
  const otp = document.getElementById("otp").value;
  const newPassword = document.getElementById("newPassword").value;

  fetch("/reset-password", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, otp, newPassword })
  })
  .then(res => res.json().then(data => ({ status: res.status, body: data })))
  .then(({ status, body }) => {
    responseMsg.innerText = body.message;
    if (status === 200) {
      responseMsg.classList.remove("text-danger");
      responseMsg.classList.add("text-success");

      setTimeout(() => {
        window.location.href = "/login";
      }, 2000);
    } else {
      responseMsg.classList.remove("text-success");
      responseMsg.classList.add("text-danger");
    }
  })
  .catch(() => {
    responseMsg.innerText = "Error resetting password.";
    responseMsg.classList.remove("text-success");
    responseMsg.classList.add("text-danger");
  });
}
