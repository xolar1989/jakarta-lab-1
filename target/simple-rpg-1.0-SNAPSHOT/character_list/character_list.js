window.addEventListener('load', () => {
    loadUserCharacters();
});

/**
 * Create new table cell with button with assigned action.
 * @param {string} text text to be displayed on button
 * @param {function} action function to be executed on button click
 * @returns {HTMLTableDataCellElement} table cell with action button
 */
function createButtonCell(text, action) {
    const td = document.createElement('td');
    const button = document.createElement('button');
    button.appendChild(document.createTextNode(text));
    button.classList.add('ui-control', 'ui-button');
    td.appendChild(button);
    button.addEventListener('click', action);
    return td;
}

/**
 * Create new table cell with hyperlink.
 * @param {string} text text to be displayed on link
 * @param {string} url link url
 */
function createLinkCell(text, url) {
    const td = document.createElement('td');
    const a = document.createElement('a');
    a.appendChild(document.createTextNode(text));
    a.href = url;
    td.appendChild(a);
    return td;
}

/**
 * Delete selected character and refresh table.
 * @param {number} character character's id
 */
function deleteCharacter(character) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 204) {
            loadUserCharacters()
        }
    };
    xhttp.open("DELETE", getContextRoot() + '/api/user/characters/' + character, true);
    xhttp.send();
}

/**
 * Create row for single character.
 * @param {{name: string, id: number}} character
 * @returns {HTMLTableRowElement} row with character data
 */
function createCharacterRow(character) {
    const tr = document.createElement('tr');

    const name = document.createElement('td');
    name.appendChild(document.createTextNode(character.name));
    tr.appendChild(name);

    tr.appendChild(createLinkCell('view', '../character_view/character_view.html?id=' + character.id));

    tr.appendChild(createLinkCell('edit', '../character_edit/character_edit.html?id=' + character.id));

    tr.appendChild(createButtonCell('delete', () => {
        deleteCharacter(character.id);
    }));

    return tr;
}

/**
 * Fetches currently logged user's characters and displays them in table.
 */
function loadUserCharacters() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let response = JSON.parse(this.responseText);
            let tbody = document.getElementById('charactersTableBody');
            clearElementChildren(tbody);
            response.characters.forEach(character => {
                tbody.appendChild(createCharacterRow(character));
            })
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/user/characters', true);
    xhttp.send();
}
