window.addEventListener('load', () => {
    const urlParams = new URLSearchParams(window.location.search);
    loadCharacter(parseInt(urlParams.get('id')));
});

/**
 * Fetches currently logged user's characters and displays them in table.
 * @param {number} id character's id
 */
function loadCharacter(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const response = JSON.parse(this.responseText);
            for (const [key, value] of Object.entries(response)) {
                const element = document.getElementById(key);
                if (element) {
                    updateElementText(element, value);
                }
            }
            document.getElementById('portrait').src = getContextRoot() + '/api/portraits/' + id;
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/user/characters/' + id, true);
    xhttp.send();
}

/**
 * Removes all children of the provided element and creates new text node inside it.
 * @param {HTMLElement} element parent element
 * @param {string} text text to be displayed
 */
function updateElementText(element, text) {
    clearElementChildren(element);
    element.appendChild(document.createTextNode(text));
}
