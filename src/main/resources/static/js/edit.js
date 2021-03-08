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


    form.setAttribute("action", "/admin/" + userId)
    modalIdInput.value = userId
    modalFirstNameInput.value = firstName
    modalLastNameInput.value = lastName
    modalAgeInput.value = age
    modalEmailInput.value = email

    for ( var i = 0, l = select.options.length, o; i < l; i++ )
    {
        o = select.options[i];
        if ( roles.indexOf( o.text ) != -1 )
        {
            o.selected = true;
        } else {
            o.selected = false;
        }
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
    modalIdInput.value = userId
    modalFirstNameInput.value = firstName
    modalLastNameInput.value = lastName
    modalAgeInput.value = age
    modalEmailInput.value = email

    for ( var i = 0, l = select.options.length, o; i < l; i++ )
    {
        o = select.options[i];
        if ( roles.indexOf( o.text ) != -1 )
        {
            o.selected = true;
        } else {
            o.selected = false;
        }
    }
})
