window.addEventListener('load', () => {
    const infoForm = document.getElementById('infoForm');
    const portraitForm = document.getElementById('portraitForm');

    infoForm.addEventListener('submit', event => updateInfoAction(event));
    portraitForm.addEventListener('submit', event => uploadPortraitAction(event));

    loadCharacter(getCharacterId());
});

/**
 *
 * @returns {number} character's ide from request path
 */
function getCharacterId() {
    const urlParams = new URLSearchParams(window.location.search);
    return parseInt(urlParams.get('id'));
}

/**
 * Fetches currently logged user's characters and updates edit form.
 * @param {number} id character's id
 */
function loadCharacter(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let response = JSON.parse(this.responseText);
            for (const [key, value] of Object.entries(response)) {
                let input = document.getElementById(key);
                if (input) {
                    input.value = value;
                }
            }
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/user/characters/' + id, true);
    xhttp.send();
}

/**
 * Action event handled for updating character info.
 * @param {Event} event dom event
 */
function updateInfoAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            loadCharacter(getCharacterId());
        }
    };
    xhttp.open('PUT', getContextRoot() + '/api/user/characters/' + getCharacterId(), true);

    const request = {
        'name': document.getElementById('name').value,
        'background': document.getElementById('background').value,
        'age': parseInt(document.getElementById('age').value)
    };

    xhttp.send(JSON.stringify(request));
}

/**
 * Action event handled for uploading character portrait.
 * @param {Event} event dom event
 */
function uploadPortraitAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.open('PUT', getContextRoot() + '/api/portraits/' + getCharacterId(), true);

    let request = new FormData();
    request.append("portrait", document.getElementById('portrait').files[0]);

    xhttp.send(request);

}
