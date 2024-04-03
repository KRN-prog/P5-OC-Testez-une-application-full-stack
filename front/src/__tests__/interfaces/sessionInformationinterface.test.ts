import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('SessionInformation interface', () => {
  it('should have correct properties', () => {
      const sessionInfo: SessionInformation = {
        id: 1,
        firstName: 'testFirstName',
        lastName: 'testLastName',
        username: 'testUsername',
        token: 'unitTestToken',
        type: 'unitTestType',
        admin: true
    };
    
    // Changer à .toHaveProperty('..')
    expect(sessionInfo).toHaveProperty('id');
    expect(sessionInfo).toHaveProperty('firstName');
    expect(sessionInfo).toHaveProperty('lastName');
    expect(sessionInfo).toHaveProperty('username');
    expect(sessionInfo).toHaveProperty('token');
    expect(sessionInfo).toHaveProperty('type');
    expect(sessionInfo).toHaveProperty('admin');
  });

  it('should have the correct property types', () => {
    const sessionInfo: SessionInformation = {
      token: 'testToken',
      type: 'testType',
      id: 1,
      username: 'testUsername',
      firstName: 'testFirstName',
      lastName: 'testLastName',
      admin: true,
    };

    // Changer à .toEqual('...')
    expect(typeof sessionInfo.token).toEqual('string');
    expect(typeof sessionInfo.type).toEqual('string');
    expect(typeof sessionInfo.id).toEqual('number');
    expect(typeof sessionInfo.username).toEqual('string');
    expect(typeof sessionInfo.firstName).toEqual('string');
    expect(typeof sessionInfo.lastName).toEqual('string');
    expect(typeof sessionInfo.admin).toEqual('boolean');
  });
});
