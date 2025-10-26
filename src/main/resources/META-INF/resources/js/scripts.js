const API_BASE_URL = 'https://5t7iw2ueuh.execute-api.us-east-1.amazonaws.com/prod';

const COGNITO_CONFIG = {
    region: 'us-east-1',
    userPoolId: 'us-east-1_wzoLfzk49',
    userPoolWebClientId: '640n3o2isl0evvhroein0apkfe',
    oauth: {
        domain: 'us-east-1wzolfzk49.auth.us-east-1.amazoncognito.com',
        scope: ['email', 'openid', 'profile'],
        redirectSignIn: 'https://lab7-frontend.s3.us-east-1.amazonaws.com/index.html',
        redirectSignOut: 'https://lab7-frontend.s3.us-east-1.amazonaws.com/index.html',
        responseType: 'code'
    }
};

let currentUser = null;
let jwtToken = null;

document.addEventListener("DOMContentLoaded", function() {
    checkAuthStatus();

    document.getElementById('login-btn').addEventListener('click', redirectToLogin);
    document.getElementById('logout-btn').addEventListener('click', logout);
    document.getElementById('post-button').addEventListener('click', createPost);
    document.getElementById('post-text').addEventListener('input', updateCharCount);
});

function checkAuthStatus() {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');

    if (code) {
        handleCognitoRedirect(code);
    } else {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            jwtToken = token;
            showApp();
        } else {
            showLogin();
        }
    }
}

function handleCognitoRedirect(code) {
    const mockToken = 'mock-jwt-token-' + Date.now();
    localStorage.setItem('jwtToken', mockToken);
    jwtToken = mockToken;

    window.history.replaceState({}, document.title, window.location.pathname);
    showApp();
}

function redirectToLogin() {
    const cognitoAuthUrl = `https://${COGNITO_CONFIG.oauth.domain}/oauth2/authorize?` +
        `client_id=${COGNITO_CONFIG.userPoolWebClientId}` +
        `&response_type=${COGNITO_CONFIG.oauth.responseType}` +
        `&scope=${COGNITO_CONFIG.oauth.scope.join('+')}` +
        `&redirect_uri=${encodeURIComponent(COGNITO_CONFIG.oauth.redirectSignIn)}`;

    window.location.href = cognitoAuthUrl;
}

function logout() {
    localStorage.removeItem('jwtToken');
    jwtToken = null;
    currentUser = null;

    const cognitoLogoutUrl = `https://${COGNITO_CONFIG.oauth.domain}/logout?` +
        `client_id=${COGNITO_CONFIG.userPoolWebClientId}` +
        `&logout_uri=${encodeURIComponent(COGNITO_CONFIG.oauth.redirectSignOut)}`;

    window.location.href = cognitoLogoutUrl;
}

function showApp() {
    document.getElementById('auth-section').style.display = 'none';
    document.getElementById('app-section').style.display = 'block';
    document.getElementById('user-info').textContent = 'Usuario autenticado';
    getPosts();
}

function showLogin() {
    document.getElementById('auth-section').style.display = 'block';
    document.getElementById('app-section').style.display = 'none';
}

function createPost() {
    const postText = document.getElementById('post-text').value.trim();
    if (postText && postText.length <= 140) {
        const postData = {
            owner: {
                username: "usuario_" + Date.now(),
                password: "temp"
            },
            content: postText
        };

        fetch(`${API_BASE_URL}/posts`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(postData)
        })
            .then(response => {
                if (response.ok) {
                    document.getElementById('post-text').value = '';
                    updateCharCount();
                    getPosts();
                    return response.text();
                }
                throw new Error('Error en el servidor');
            })
            .then(data => console.log('Post creado:', data))
            .catch(error => {
                console.error('Error:', error);
                alert('Error al crear el post');
            });
    } else {
        alert('El post debe tener entre 1 y 140 caracteres');
    }
}

function getPosts() {

    fetch(`${API_BASE_URL}/posts`)
        .then(response => {
            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);
            return response.json();
        })
        .then(posts => {
            const postList = document.getElementById('post-list');
            postList.innerHTML = '';

            if (!posts || posts.length === 0) {
                postList.innerHTML = `
                    <div class="no-posts">
                        <h3>¡No hay posts todavía!</h3>
                        <p>Sé el primero en publicar algo.</p>
                    </div>
                `;
                return;
            }

            posts.forEach((post, index) => {

                const username = post.owner?.username || 'Anónimo';
                const content = post.content || 'Sin contenido';

                const postElement = document.createElement('div');
                postElement.className = 'post';
                postElement.innerHTML = `
                    <div class="post-header">
                        <strong>@${username}</strong>
                    </div>
                    <div class="post-content">${content}</div>
                    <div class="post-time">${new Date().toLocaleString()}</div>
                `;

                postList.appendChild(postElement);
            });
        })
        .catch(error => {
            console.error('Error cargando posts:', error);
        });
}


function updateCharCount() {
    const length = document.getElementById('post-text').value.length;
    const charCount = document.getElementById('char-count');
    charCount.textContent = `${length}/140`;
    charCount.style.color = length > 140 ? 'red' : 'gray';
}

updateCharCount();