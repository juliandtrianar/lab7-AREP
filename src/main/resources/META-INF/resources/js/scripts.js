document.addEventListener("DOMContentLoaded", function() {
    getPosts();

    document.getElementById('post-button').addEventListener('click', function() {
        const postText = document.getElementById('post-text').value;
        if (postText.trim() !== ''  && postText.length <= 140) {

            const tempUser = {
                username: 'usuario_temporal',
                password: 'contraseña_temporal'
            };

            const postData = {
                owner: tempUser,
                content: postText
            };
            fetch('/posts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(postData)
            })
            .then(response => {
                if (response.ok) {
                    document.getElementById('post-text').value = '';
                    getPosts();
                } else {
                    console.error('Error al enviar el post:', response.statusText);
                }
            })
            .catch(error => console.error('Error al enviar el post:', error));
        } else {
            console.error('El campo de texto está vacío');
        }
    });
});


function getPosts() {
    fetch('/streams')
        .then(response => response.json())
        .then(posts => {
            document.getElementById('post-list').innerHTML = '';
            posts.reverse().forEach(post => {
                addPostToPage(post);
            });
        })
        .catch(error => console.error('Error al obtener los posts:', error));
}

function addPostToPage(post) {
    const postDiv = document.createElement('div');
    postDiv.className = 'post';
    const postText = document.createElement('p');
    postText.textContent = post.content; 
    postDiv.appendChild(postText);
    document.getElementById('post-list').appendChild(postDiv);
}