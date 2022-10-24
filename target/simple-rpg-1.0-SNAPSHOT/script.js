window.addEventListener('load', () => {
    const loginForm = document.getElementById('loginForm');
    const logoutForm = document.getElementById('logoutForm');

    loginForm.addEventListener('submit', event => loginAction(event));
    logoutForm.addEventListener('submit', event => logoutAction(event));

    checkLoggedUser();
});

/**
 * Makes an AJAX call and checks if there is user in http session. If yes then logout form is displayed. Otherwise the
 * login form is displayed.
 */
function checkLoggedUser() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const loginForm = document.getElementById('loginForm');
            const logoutForm = document.getElementById('logoutForm');
            const username = document.getElementById('username');

            const response = JSON.parse(this.responseText);

            clearElementChildren(username);

            if (response.login) {
                loginForm.classList.add('hidden');
                logoutForm.classList.remove('hidden');
                username.appendChild(document.createTextNode(response.login));
            } else {
                loginForm.classList.remove('hidden');
                logoutForm.classList.add('hidden');
            }
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/user', true);
    xhttp.send();
}

/**
 * Clears all children of the provided element
 * @param {HTMLElement} element parent element
 */
function clearElementChildren(element) {
    while (element.firstChild) {
        element.removeChild(element.firstChild);
    }
}

/**
 * Action event handled for pressing login button.
 * @param {Event} event dom event
 */
function loginAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            document.getElementById('login').value = '';
            document.getElementById('password').value = '';
            checkLoggedUser();
        }
    };
    xhttp.open('POST', getContextRoot() + '/api/user/login', true);

    const request = new FormData();
    request.append('login', document.getElementById('login').value);
    request.append('password', document.getElementById('password').value);

    xhttp.send(request);
}

/**
 * Action event handled for pressing logout button.
 * @param {Event} event
 */
function logoutAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            checkLoggedUser();
        }
    };
    xhttp.open('POST', getContextRoot() + '/api/user/logout', true);
    xhttp.send();
}

/**
 *
 * @returns {string} context root (application name) to be used as a root for rest requests
 */
function getContextRoot() {
    return '/' + location.pathname.split('/')[1];
}
