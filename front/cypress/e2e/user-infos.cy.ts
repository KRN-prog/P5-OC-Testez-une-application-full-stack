describe('Test de navigation utilisateur', () => {
    it('Se connecte et accède à la page de profil', () => {
        // Visite la page de connexion
        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            body: {
                admin: true,
                created_at: [2024,3,26,16,8,47],
                email: 'yoga@studio.com',
                firstName: 'firstName',
                id: 1,
                lastName: 'lastName',
                updatedAt: [2024,3,26,16,8,47]
            },
        }).as('login')

        cy.intercept('GET', '/api/session', {
            statusCode: 200,
            body: {
                admin: true,
                created_at: [2024,3,26,16,8,47],
                email: 'yoga@studio.com',
                firstName: 'firstName',
                id: 1,
                lastName: 'lastName',
                updatedAt: [2024,3,26,16,8,47]
            },
        }).as('session')

        cy.intercept('GET', '/api/user/1',{
            statusCode: 200,
            body: {
                admin: true,
                created_at: [2024,3,26,16,8,47],
                email: 'yoga@studio.com',
                firstName: 'firstName',
                id: 1,
                lastName: 'lastName',
                updatedAt: [2024,3,26,16,8,47]
            },
        }).as('getUser');
  


        // Saisie de l'email et du mot de passe et soumission du formulaire de connexion
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    
        // Vérifie la redirection vers la page des sessions
        cy.url().should('include', '/sessions');

        // Clique sur le lien "Account"
        cy.contains('Account').click();

        cy.wait('@getUser').then((interception) => {
            // Vérifier que la requête a été interceptée avec succès
            expect(interception.response.statusCode).to.eq(200);
            expect(interception.response.body).to.deep.equal({
                admin: true,
                created_at: [2024,3,26,16,8,47],
                email: 'yoga@studio.com',
                firstName: 'firstName',
                id: 1,
                lastName: 'lastName',
                updatedAt: [2024,3,26,16,8,47]
            });
        });

        cy.contains('yoga@studio.com').should('be.visible');

        // Vérifie la redirection vers la page "Me"
        cy.url().should('include', '/me');
    
    });
  });
  