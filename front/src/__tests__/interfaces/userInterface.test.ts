import { User } from "src/app/interfaces/user.interface";

describe("User interface", () => {
    it("Should have the correct properties", () => {
        const actualDate = new Date();
        const user: User = {
            id: 1,
            email: "jean@mail.com",
            lastName: "Doe",
            firstName: "jean",
            admin: false,
            password: "test!1234",
            createdAt: actualDate,
            updatedAt: actualDate
        }
        
        // Changer à .toHaveProperty('..')
        expect(user).toHaveProperty('id');
        expect(user).toHaveProperty('email');
        expect(user).toHaveProperty('lastName');
        expect(user).toHaveProperty('firstName');
        expect(user).toHaveProperty('admin');
        expect(user).toHaveProperty('password');
        expect(user).toHaveProperty('createdAt');
        expect(user).toHaveProperty('updatedAt');
    })

    it('should have the correct property types', () => {
        const actualDate = new Date();
        const user: User = {
            id: 1,
            email: "jean@mail.com",
            lastName: "Doe",
            firstName: "jean",
            admin: false,
            password: "test!1234",
            createdAt: actualDate,
            updatedAt: actualDate
        };
    
        // Changer à .toEqual('...')
        expect(typeof user.id).toEqual('number');
        expect(typeof user.lastName).toEqual('string');
        expect(typeof user.firstName).toEqual('string');
        expect(typeof user.admin).toEqual('boolean');
        expect(typeof user.password).toEqual('string');
        expect(typeof user.createdAt).toEqual('object');
        expect(typeof user.updatedAt).toEqual('object');
    });
})