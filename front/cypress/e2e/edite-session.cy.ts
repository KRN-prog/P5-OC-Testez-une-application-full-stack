describe('Edite sessesion spec', () => {
    it('Edite successfully', () => {
        cy.visit('/login');

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
        }).as('sessions')

        cy.intercept('GET', '/api/session/1',{
            satusCode: 200,
            body:
                {
                    id: 1,
                    name: "session test",
                    date: 1711497600000,
                    teacher_id: 1,
                    description: "test",
                    users: [],
                    createdAt: [2024,3,27,18,56,20],
                    updatedAt: [2024,3,27,18,56,20]
                }
        }).as('session')

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

        cy.intercept('PUT', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 2,
                name: "My new session",
                date: 1712102400000,
                teacher_id: 1,
                description: "Test",
                users: [],
                createdAt: [2024,4,2,1,41,29],
                updatedAt: [2024,4,2,1,41,29]
            }
        }).as('editSession');

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')

        /* ======================== */

        cy.contains('Rentals available').should('be.visible');

        cy.get('mat-icon').eq(2).click();

        cy.url().should('include', '/sessions/update/1')

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

        cy.get('input[formControlName=name]').clear().type("My new session");

        cy.get('form').submit();

        cy.wait('@editSession').then((interception) => {
            // Vérifier que la requête a été interceptée avec succès
            expect(interception.response.statusCode).to.eq(200);
            expect(interception.response.body).to.deep.equal({
                id: 2,
                name: "My new session",
                date: 1712102400000,
                teacher_id: 1,
                description: "Test",
                users: [],
                createdAt: [2024,4,2,1,41,29],
                updatedAt: [2024,4,2,1,41,29]
            });
        });

        cy.contains('Session updated !').should('be.visible');
        cy.contains('Close').should('be.visible').click();

        cy.url().should('include', '/sessions');
    })
})