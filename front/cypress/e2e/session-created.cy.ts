describe('Session create spec', () => {
    it('Session create successfull', () => {
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

        cy.intercept('GET', '/api/session',{
            satusCode: 200,
            body: [
                {
                    id: 1,
                    name: "session test",
                    date: 1711497600000,
                    teacher_id: 1,
                    description: "test",
                    users: [],
                    createdAt: [2024,3,27,18,56,20],
                    updatedAt: [2024,3,27,18,56,20]
                },
                {
                    id: 2,
                    name: "John Doe",
                    date: 1712102400000,
                    teacher_id: 1,
                    description: "This is a wonderful description",
                    users: [],
                    createdAt: [2024,4,2,1,41,29],
                    updatedAt: [2024,4,2,1,41,29]
                }
            ],
        }).as('sessionCreated')

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

        cy.intercept('POST', '/api/session', {
            statusCode: 200,
            body: {
                id: 2,
                name: "John Doe",
                date: 1712102400000,
                teacher_id: 1,
                description: "This is a wonderful description",
                users: [],
                createdAt: [2024,4,2,1,41,29],
                updatedAt: [2024,4,2,1,41,29]
            }
        }).as('submitSession');

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')

        /* ======================== */

        cy.contains('Rentals available').should('be.visible');

        cy.get('.mat-raised-button').eq(0).click();

        cy.url().should('include', '/sessions/create')

        cy.contains('Create session').should('be.visible');

        cy.get('input[formControlName=name]').type("John Doe");

        cy.get('input[formControlName=date]').type('2024-03-29');

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
                }
            ]);
        });

        cy.get('.mat-select-placeholder').click();

        cy.get('mat-option').contains(' Margot DELAHAYE ').click();
        
        cy.get('[formControlName="description"]').type('This is a wonderful description');

        cy.get('form').submit();

        cy.wait('@submitSession').then((interception) => {
            // Vérifier que la requête a été interceptée avec succès
            expect(interception.response.statusCode).to.eq(200);
            expect(interception.response.body).to.deep.equal({
                id: 2,
                name: "John Doe",
                date: 1712102400000,
                teacher_id: 1,
                description: "This is a wonderful description",
                users: [],
                createdAt: [2024,4,2,1,41,29],
                updatedAt: [2024,4,2,1,41,29]
            });
        });

        cy.url().should('include', '/sessions')

        cy.contains('Session created !').should('be.visible');
        cy.contains('Close').should('be.visible').click();
    })
});