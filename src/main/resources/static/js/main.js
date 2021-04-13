let isAdmin = false;
let usersURL = "http://localhost:8080/users";
let respData;
let roles = {};

$(document).ready(function () {
        document.getElementById("addNewUserBtn").disabled = true;
        document.getElementById("usersContainer").hidden = true;
        document.getElementById("logout").hidden = true;
    }
);

function getUsers() {
    Requests.get(usersURL, {}, true,
        {
            success: function (resp) {
                console.log(resp);
                let table = document.getElementById("usersTable").getElementsByTagName("tbody")[0];
                table.remove();
                let tbody = document.createElement('tbody');
                tbody.className = "table-light";
                document.getElementById("usersTable").appendChild(tbody);
                drawUsersTable(resp);
                console.log(isAdmin);
                if (isAdmin)
                    doButtonsController(false);
                else
                    doButtonsController(true);
            }
        }
    );
}

function getUser(URL) {
    Requests.get(URL, {}, true,
        {
            success: function (resp) {
                console.log(resp);
                inputDataInModal(resp);
            }
        }
    );
}

function deleteUser(URL, id) {
    Requests.post(URL, {"id": id}, true,
        {
            success: function (resp) {
                console.log(resp);
                getUsers();
            }
        }, "DELETE"
    );
}

function drawUsersTable(resp) {
    let rolesStr;
    for (let i = 0; i < resp.length; i++) {
        rolesStr = ""
        for (let j = 0; j < resp[i]["roles"].length; j++) {
            rolesStr += " " + resp[i]["roles"][j]["roleName"];
        }
        addRow(resp[i]["id"], resp[i]["login"], resp[i]["password"], rolesStr);
    }
}

function addRow(id, login, pass, roles) {
    let table = document.getElementById("usersTable").getElementsByTagName("tbody")[0];
    let tr = table.insertRow(table.rows.length);
    let td;

    let idTd = document.createElement("td");
    idTd.scope = "row";
    idTd.innerText = id;
    tr.insertAdjacentElement("beforeend", idTd)

    let LoginTd = document.createElement("td");
    LoginTd.innerText = login;
    tr.insertAdjacentElement("beforeend", LoginTd);

    let passTd = document.createElement("td");
    passTd.innerText = pass;
    tr.insertAdjacentElement("beforeend", passTd);

    let rolesTd = document.createElement("td");
    rolesTd.innerText = roles;
    tr.insertAdjacentElement("beforeend", rolesTd);

    let editButton = document.createElement("button");
    editButton.className = "btn btn-small btn-primary";
    editButton.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-pencil-square\" viewBox=\"0 0 16 16\">\n" +
        "  <path d=\"M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z\"></path>\n" +
        "  <path fill-rule=\"evenodd\" d=\"M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z\"></path>\n" +
        "</svg>";
    editButton.type = "submit";
    editButton.setAttribute("href", "#userModal");
    editButton.setAttribute("data-bs-toggle", "modal");
    editButton.addEventListener("click", () => {
        drawModalForm(id);
    });
    td = tr.insertCell(4);
    td.insertAdjacentElement("beforeend", editButton);

    let delButton = document.createElement("button");
    delButton.className = "btn btn-small btn-danger";
    delButton.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-trash\" viewBox=\"0 0 16 16\">\n" +
        "  <path d=\"M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z\"></path>\n" +
        "  <path fill-rule=\"evenodd\" d=\"M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z\"></path>\n" +
        "</svg>";
    delButton.addEventListener("click", () => {
        deleteUser(usersURL + "/" + id, id);
    });
    td.insertAdjacentElement("beforeend", delButton);
}


function getRoles() {
    Requests.get("http://localhost:8080/roles", {}, true,
        {
            success: function (resp) {
                roles = resp;
            }
        }
    );
}

function drawModalForm(action) {
    clearModal();

    let elem = document.getElementById("roles");
    while (elem.children.length > 0) {
        elem.removeChild(elem.children[0]);
    }

    for (let i = 0; i < roles.length; i++) {
        addNewCheckBoxItem("roles", roles[i]["roleId"], roles[i]["roleName"]);
    }

    let old_element = document.getElementById("saveModalBtn");
    let new_element = old_element.cloneNode(true);
    old_element.parentNode.replaceChild(new_element, old_element);

    let saveBtn = document.getElementById("saveModalBtn");

    if (action.id === "addNewUserBtn") {
        document.getElementById("action").innerText = "create";
        saveBtn.addEventListener("click", () => {
            postUserSave(usersURL);
        });
    } else {
        let url = usersURL + "/" + action;
        getUser(url);
        document.getElementById("action").innerText = "edit";
        saveBtn.addEventListener("click", () => {
            patchUserSave(url);
        });
    }
}

function addNewCheckBoxItem(elem, id, value) {
    let parentDiv = document.getElementById(elem);

    let div = document.createElement("div");
    div.className = "form-check";
    div.style.display = "inline-flex";

    let input = document.createElement("input");
    input.id = id;
    input.className = "form-check-input";
    input.type = "checkbox";

    let label = document.createElement("label");
    label.className = "form-check-label";
    label.for = id;
    label.innerText = value;

    div.insertAdjacentElement("beforeend", input);
    input.insertAdjacentElement("afterend", label);
    parentDiv.insertAdjacentElement("beforeend", div);
}


function postUserSave(URL) {
    Requests.post(URL, getParams(), true,
        {
            success: function (resp) {
                console.log(resp);
                getUsers();
            }
        }, "POST"
    );
}

function patchUserSave(URL) {
    Requests.post(URL, getParams(), true,
        {
            success: function (resp) {
                console.log(resp);
                getUsers();
            }
        }, "PATCH"
    );
}

function getParams() {
    let data = {}
    if (document.getElementById("action").innerText === "edit")
        data['id'] = getValue("userModalId");
    data['login'] = getValue("login");
    data['password'] = getValue("password");

    let rolesElem = document.getElementById("roles");
    let roles = [];
    let role = {};
    for (let i = 0; i < rolesElem.children.length; i++) {
        if (rolesElem.children[i].getElementsByTagName("input")[0].checked) {
            role["roleId"] = rolesElem.children[i].getElementsByTagName("input")[0].id;
            role["roleName"] = rolesElem.children[i].getElementsByTagName("label")[0].innerText;
            roles.push(role);
        }

    }
    data['roles'] = roles;
    return JSON.stringify(data);
}

function clearModal() {
    document.getElementById("userModalId").value = null;
    document.getElementById("login").value = null;
    document.getElementById("password").value = null;
    let rolesElem = document.getElementById("roles")
    for (let i = 0; i < rolesElem.children.length; i++) {
        rolesElem.children[i].getElementsByTagName("input")[0].checked = false;

    }
}

function inputDataInModal(data) {
    document.getElementById("userModalId").value = data["id"];
    document.getElementById("login").value = data["login"];
    document.getElementById("password").value = data["password"];
    let rolesElem = document.getElementById("roles")
    for (let i = 0; i < data["roles"].length; i++) {
        for (let j = 0; j < rolesElem.children.length; j++) {
            if (data["roles"][i]["roleName"] === rolesElem.children[j].getElementsByTagName("label")[0].innerText)
                rolesElem.children[j].getElementsByTagName("input")[0].checked = true;
        }
    }
}

function loginInApp() {
    Requests.post("http://localhost:8080/login", getParamsAuth(), true,
        {
            success: function (resp) {
                console.log(resp);
                document.getElementById("usersContainer").hidden = false;
                document.getElementById("logout").hidden = false;
                clearModalAuth();
                let counter = 0;
                while (counter < resp["roles"].length) {
                    if (resp["roles"][counter]["roleName"] === "ROLE_ADMIN") {
                        isAdmin = true;
                        break;
                    } else
                        isAdmin = false;
                    counter++;
                }
                getRoles();
                getUsers();
                document.getElementById("helloUser").hidden = false;
                document.getElementById("helloUserText").innerText = "Hello, " + resp["login"];
                document.getElementById("loginBar").hidden = true;
            }
        }, "POST"
    );
}

function getParamsAuth() {
    let data = {}
    data['login'] = getValue("loginAuth");
    data['password'] = getValue("passwordAuth");
    return JSON.stringify(data);
}

function clearModalAuth() {
    document.getElementById("loginAuth").value = null;
    document.getElementById("passwordAuth").value = null;
}

function registerInApp() {
    Requests.post("http://localhost:8080/registration", getParamsReg(), true,
        {
            success: function (resp) {
                console.log(resp);
                clearModalReg();
                getUsers();
            }
        }, "POST"
    );
}

function getParamsReg() {
    let data = {}
    data['login'] = getValue("loginReg");
    data['password'] = getValue("passwordReg");
    return JSON.stringify(data);
}

function clearModalReg() {
    document.getElementById("loginReg").value = null;
    document.getElementById("passwordReg").value = null;
}

function logout() {
    Requests.post("http://localhost:8080/logout", {}, true,
        {
            success: function (resp) {
                console.log(resp);
                document.getElementById("usersContainer").hidden = true;
                document.getElementById("logout").hidden = true;
                document.getElementById("helloUser").hidden = true;
                document.getElementById("helloUserText").innerText = "";
                document.getElementById("loginBar").hidden = false;
            }
        }, "POST"
    );
}

function getValue(id) {
    let select = document.getElementById(id);
    return select.value;
}

function doButtonsController(action) {
    let items = document.getElementsByTagName("button");
    for (let i = 0; i < items.length; i++) {
        if (!items[i].hasAttribute("data-bs-dismiss")) {
            items[i].disabled = action;
            items[i].hidden = action;
        }
    }
}