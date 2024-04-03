describe('Go to create session spec', () => {
    it('Go to create session successfull', () => {
        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        })

        cy.intercept('GET', '/api/teacher',{
            statusCode: 200,
            body: [{
                id: 1,
                lastName: "DELAHAYE",
                firstName: "Margot",
                createdAt: [2024,3,26,16,8,47],
                updatedAt: [2024,3,26,16,8,47],
            },
            {
                id: 2,
                lastName: "THIERCELIN",
                firstName: "Hélène",
                createdAt: [2024,3,26,16,8,47],
                updatedAt: [2024,3,26,16,8,47],
            }],
        }).as('getTeachers');

        cy.intercept(
        {
            method: 'GET',
            url: '/api/session',
        },
        []).as('session')

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')

        /* ======================== */

        cy.contains('Rentals available').should('be.visible');

        cy.get('.mat-raised-button').eq(0).click();

        cy.wait('@getTeachers').then((interception) => {
            // Vérifier que la requête a été interceptée avec succès
            expect(interception.response.statusCode).to.eq(200);
            expect(interception.response.body).to.deep.equal([
                {
                    id: 1,
                    lastName: "DELAHAYE",
                    firstName: "Margot",
                    createdAt: [2024,3,26,16,8,47],
                    updatedAt: [2024,3,26,16,8,47],
                },
                {
                    id: 2,
                    lastName: "THIERCELIN",
                    firstName: "Hélène",
                    createdAt: [2024,3,26,16,8,47],
                    updatedAt: [2024,3,26,16,8,47],
            }]);
        });

        cy.contains('Create session').should('be.visible');

        cy.url().should('include', '/sessions/create')
    })
});