let ressourcesDiffusables = new Vue({
    el: '#ressourcesDiffusables',
    data: {
        erreur: '',
        ressources: [],
        nextPage: 0
    },
    methods: {
        getRessourcesDiffusables: function () {
            while (ressourcesDiffusables.getRessourcesDiffusablesPage(ressourcesDiffusables.nextPage) && ressourcesDiffusables.nextPage < 10) {
                ressourcesDiffusables.nextPage++;
            }
        },
        getRessourcesDiffusablesPage: function (page) {
            try {
                let request = new XMLHttpRequest();

                request.onreadystatechange = function () {
                    try {
                        if (request.readyState !== 4) { // Request not DONE.
                            return;
                        }

                        if (request.status === 0) {
                            // Timed out.
                            throw new Error("Timed out.");
                        } else if (request.status === 404) {
                            // 404 Error.
                            throw new Error("404 Error.");
                        } else if (request.status !== 200 && request.status !== 304) {
                            // Other failure.
                            throw new Error("Failed.");
                        } else {
                            ressourcesDiffusables.ressources = ressourcesDiffusables.ressources.concat(JSON.parse(request.responseText));
                        }
                    } catch (error) {
                        ressourcesDiffusables.erreur = error;
                    }
                }

                request.open(
                    'GET',
                    'http://localhost:8080/mediacentre/api/ressources-diffusables?page=' + page,
                    true
                );
                request.send()

                return true;
            } catch (error) {
                this.erreur = error;

                return false;
            }
        }
    }
})
