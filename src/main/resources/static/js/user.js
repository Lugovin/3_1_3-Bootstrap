
document.addEventListener('DOMContentLoaded', loadData);

// Функция для заполнения таблицы юзеров
async function loadData() {
    let response = await fetch('/auth') //<-- получаем аутентифицированного пользователя
    let data = await response.json()// <-- получаем данные в формает json
    let authEmailMess = document.getElementById("authEmailMess");
    authEmailMess.innerText = data.email + " with roles: " + data.stringRoles;
    // Создаем новую строку таблицы юзеров
    const table = document.getElementById('usersTable');
    const row = table.insertRow();
    const idCell = row.insertCell();
    const nameCell = row.insertCell();
    const lasNameCell = row.insertCell();
    const ageCell = row.insertCell();
    const emailCell = row.insertCell();
    const roleCell = row.insertCell();
    row.id = `userRow${data.id}`;
    idCell.textContent = data.id;
    nameCell.textContent = data.name;
    lasNameCell.textContent = data.lastName;
    ageCell.textContent = data.age;
    emailCell.textContent = data.email;
    roleCell.textContent = data.stringRoles;
}