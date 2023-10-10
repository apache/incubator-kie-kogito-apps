import { ANONYMOUS_USER, User, UserContext } from '../auth';

export class TestUserContext implements UserContext {
  public readonly user: User;
  constructor() {
    this.user = ANONYMOUS_USER;
  }

  getCurrentUser(): User {
    return this.user;
  }
}
