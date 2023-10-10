/**
 * Definition of a kogito app user.
 */
export interface User {
  /**
   * Identifier of the user.
   */
  id: string;

  /**
   * List of groups/roles the user belongs to.
   */
  groups: string[];
}

/**
 * Definition of a kogito app UserContext.
 */
export interface UserContext {
  /**
   * Retrieves the user that is currently logged to the app.
   */
  getCurrentUser(): User;
}

/**
 * Adds logout support to a UserContext
 */
export interface LogoutUserContext extends UserContext {
  /**
   * Logs out the current user
   */
  logout();
}

export class DefaultUser implements User {
  public readonly id: string;
  public readonly groups: string[];

  public constructor(id: string, groups: string[]) {
    this.id = id;
    this.groups = groups;
  }
}

export const ANONYMOUS_USER = new DefaultUser('Dev User', []);

export const supportsLogout = (userContext: UserContext): boolean => {
  return 'logout' in userContext;
};
