var editModal = document.getElementById('editModal')
editModal.addEventListener('show.bs.modal', function (event) {

    var button = event.relatedTarget
    var userId = button.getAttribute('data-bs-userid')
    var firstName = button.getAttribute('data-bs-firstname')
    var lastName = button.getAttribute('data-bs-lastname')
    var age = button.getAttribute('data-bs-age')
    var email = button.getAttribute('data-bs-email')
    var roles = button.getAttribute('data-bs-roles').split(' ')

    var modalIdInput = editModal.querySelector('.modal-body #idEdit')
    var modalFirstNameInput = document.getElementById('firstNameEdit')
    var modalLastNameInput = document.getElementById('lastNameEdit')
    var modalAgeInput = document.getElementById('ageEdit')
    var modalEmailInput = document.getElementById('emailEdit')
    var select = document.getElementById('select-roles')
    var form = document.getElementById('edit-form')

    form.setAttribute('data-bs-edituserid', userId);


    form.setAttribute("action", "/admin/" + userId)
    modalIdInput.value = userId
    modalFirstNameInput.value = firstName
    modalLastNameInput.value = lastName
    modalAgeInput.value = age
    modalEmailInput.value = email

    for (var i = 0, l = select.options.length, o; i < l; i++) {
        o = select.options[i];
        o.selected = roles.indexOf(o.text) != -1;
    }
})

var deleteModal = document.getElementById('deleteModal')
deleteModal.addEventListener('show.bs.modal', function (event) {

    var button = event.relatedTarget
    var userId = button.getAttribute('data-bs-userid')
    var firstName = button.getAttribute('data-bs-firstname')
    var lastName = button.getAttribute('data-bs-lastname')
    var age = button.getAttribute('data-bs-age')
    var email = button.getAttribute('data-bs-email')
    var roles = button.getAttribute('data-bs-roles').split(' ')

    var modalIdInput = deleteModal.querySelector('.modal-body #idDelete')
    var modalFirstNameInput = document.getElementById('firstNameDelete')
    var modalLastNameInput = document.getElementById('lastNameDelete')
    var modalAgeInput = document.getElementById('ageDelete')
    var modalEmailInput = document.getElementById('emailDelete')
    var select = document.getElementById('select-roles-delete')
    var form = document.getElementById('delete-form')


    form.setAttribute("action", "/admin/" + userId)
    form.setAttribute("data-userid", userId);
    modalIdInput.value = userId
    modalFirstNameInput.value = firstName
    modalLastNameInput.value = lastName
    modalAgeInput.value = age
    modalEmailInput.value = email

    for (var i = 0, l = select.options.length, o; i < l; i++) {
        o = select.options[i];
        o.selected = roles.indexOf(o.text) != -1;
    }
})

function fillUsersTable() {
    $("#users > tbody").empty();
    $.ajax({
        url: "/api/users",
        method: "GET",
        success: function (data) {
            $(function () {
                $.each(data, function (i, item) {
                    $('<tr>').append(
                        $('<td>').text(item.id),
                        $('<td>').text(item.firstName),
                        $('<td>').text(item.lastName),
                        $('<td>').text(item.age),
                        $('<td>').text(item.email),
                        $('<td>').text(item.rolesAsString),
                        $('<td>').append(
                            $('<button>')
                                .attr("type", "button")
                                .attr("class", "btn btn-info")
                                .attr("data-bs-toggle", "modal")
                                .attr("data-bs-target", "#editModal")
                                .attr("data-bs-userid", item.id)
                                .attr("data-bs-firstname", item.firstName)
                                .attr("data-bs-lastname", item.lastName)
                                .attr("data-bs-age", item.age)
                                .attr("data-bs-email", item.email)
                                .attr("data-bs-roles", item.rolesAsString)
                                .text("Edit")
                        ),
                        $('<td>').append(
                            $('<button>')
                                .attr("type", "button")
                                .attr("class", "btn btn-danger")
                                .attr("data-bs-toggle", "modal")
                                .attr("data-bs-target", "#deleteModal")
                                .attr("data-bs-userid", item.id)
                                .attr("data-bs-firstname", item.firstName)
                                .attr("data-bs-lastname", item.lastName)
                                .attr("data-bs-age", item.age)
                                .attr("data-bs-email", item.email)
                                .attr("data-bs-roles", item.rolesAsString)
                                .text("Delete")
                        ),
                    ).appendTo('#users > tbody');
                })
            })
        }
    })
}

$(function () {
    $('#delete-form').on("submit", function (e) {
        e.preventDefault();
        const id = document.getElementById("delete-form").getAttribute("data-userid");
        $.ajax({
            type: "DELETE",
            url: "/api/users/" + id,
            success: function () {
                $('#deleteModal').modal('hide');
                fillUsersTable();
            }
        })
    })
})

$(function () {
    $('#edit-form').on("submit", function (e) {
        e.preventDefault();
        const data = new FormData(document.getElementById('edit-form'));
        const user = Object.fromEntries(data.entries());
        user.roles = data.getAll("roles");
        const id = document.getElementById('edit-form').getAttribute('data-bs-edituserid');
        $.ajax({
            type: "PATCH",
            url: "api/users/" + id,
            data: JSON.stringify(user),
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            complete: function () {
                fillUsersTable();
                $('#editModal').modal('hide');
            }
        })
    })
});

$(function () {
    $('#newuser-form').on("submit", function (e) {
        e.preventDefault();
        const data = new FormData(document.getElementById('newuser-form'));
        const user = Object.fromEntries(data.entries());
        user.roles = data.getAll("roles");
        $.ajax({
            type: "POST",
            url: "api/users",
            data: JSON.stringify(user),
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            complete: function () {
                fillUsersTable();
                $('#nav-tab li:first-child a').tab('show');
            }
        })
    })
})

function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

$(document).ready(fillUsersTable())


