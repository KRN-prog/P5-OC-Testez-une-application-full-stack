import { AuthGuard } from './auth.guard';
import { Router } from '@angular/router';
import { SessionService } from '../services/session.service';
import { expect } from '@jest/globals';

describe('AuthGuard', () => {
  let guard: AuthGuard;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(() => {
    router = {} as Router; // Mock du Router
    sessionService = {
      isLogged: false, // Mock de SessionService
      // Autres méthodes mock de SessionService si nécessaire
    } as SessionService;

    guard = new AuthGuard(router, sessionService); // Instanciation du garde
  });

  it('should allow activation if user is logged in', () => {
    sessionService.isLogged = true; // Simuler un utilisateur connecté

    const result = guard.canActivate(); // Appeler canActivate

    expect(result).toBe(true); // Vérifier si la garde autorise l'activation
  });

  it('should redirect to login page if user is not logged in', () => {
    sessionService.isLogged = false; // Simuler un utilisateur non connecté
    const navigateSpy = jest.spyOn(router, 'navigate'); // Espionner la méthode navigate du Router

    const result = guard.canActivate(); // Appeler canActivate

    expect(result).toBe(false); // Vérifier si la garde n'autorise pas l'activation
    expect(navigateSpy).toHaveBeenCalledWith(['login']); // Vérifier si la redirection vers la page de connexion a été appelée
  });
});
