import { Teacher } from "src/app/interfaces/teacher.interface";

describe("Teacher interface", () => {
    it("should have the correct properties", () => {
        const actualDate = new Date();
        const teacher: Teacher = {
            id: 1,
            lastName: "Doe",
            firstName: "Jean",
            createdAt: actualDate,
            updatedAt: actualDate
        };

        // Changer à .toHaveProperty('..')
        expect(teacher).toHaveProperty('id');
        expect(teacher).toHaveProperty('lastName');
        expect(teacher).toHaveProperty('firstName');
        expect(teacher).toHaveProperty('createdAt');
        expect(teacher).toHaveProperty('updatedAt');
    })

    it('should have the correct property types', () => {
        const actualDate = new Date();
        const teacher: Teacher = {
            id: 1,
            lastName: "Doe",
            firstName: "Jean",
            createdAt: actualDate,
            updatedAt: actualDate
        };
    
        // Changer à .totoEqual('...')
        expect(typeof teacher.id).toEqual('number');
        expect(typeof teacher.lastName).toEqual('string');
        expect(typeof teacher.firstName).toEqual('string');
        expect(typeof teacher.createdAt).toEqual('object');
        expect(typeof teacher.updatedAt).toEqual('object');
    });
})