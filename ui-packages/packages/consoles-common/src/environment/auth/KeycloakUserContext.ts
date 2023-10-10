import { User, DefaultUser, LogoutUserContext } from './Auth';

export interface KeycloakInfo {
  userName: string;
  roles: string[];
  token: string;
  tokenMinValidity: number;
  logout: () => void;
}
export class KeycloakUserContext implements LogoutUserContext {
  private readonly currentUser: User;
  private token: string;
  private readonly tokenMinValidity: number;
  private handleLogout: () => void;
  constructor(keycloakInfo: KeycloakInfo) {
    this.currentUser = new DefaultUser(
      keycloakInfo.userName,
      keycloakInfo.roles
    );
    this.tokenMinValidity = keycloakInfo.tokenMinValidity;
    this.token = keycloakInfo.token;
    this.handleLogout = keycloakInfo.logout;
  }

  logout() {
    this.handleLogout();
  }

  getCurrentUser(): User {
    return this.currentUser;
  }

  getToken(): string {
    return this.token;
  }

  setToken(token: string): void {
    this.token = token;
  }

  getTokenMinValidity(): number {
    return this.tokenMinValidity;
  }
}
