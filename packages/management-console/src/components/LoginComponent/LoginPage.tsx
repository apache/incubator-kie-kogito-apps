import React, {useState} from 'react';
import { LoginForm, LoginMainFooterBandItem, LoginPage, BackgroundImageSrc } from '@patternfly/react-core';
import { ExclamationCircleIcon } from '@patternfly/react-icons';
const brandImage = require('../../static/kogito_logo.png');
const backgroundImage = require('../../static/black_background.png');
import './LoginPage.css';

export interface IOwnProps {}

export interface IStateprops {
  showHelperText: boolean;
  usernameValue: string;
  isValidUsername: boolean;
  passwordValue: string;
  isValidPassword: boolean;
  isRememberMeChecked: boolean;
  isAuthenticated: boolean;
}
const images = {
  [BackgroundImageSrc.lg]: backgroundImage,
  [BackgroundImageSrc.sm]: backgroundImage,
  [BackgroundImageSrc.sm2x]: backgroundImage,
  [BackgroundImageSrc.xs]: backgroundImage,
  [BackgroundImageSrc.xs2x]: backgroundImage,
  [BackgroundImageSrc.filter]: '/assets/images/background-filter.svg#image_overlay'
};

// constructor(props: IOwnProps) {
//   super(props);
//     state = {
//     showHelperText: false,
//     usernameValue: '',
//     isValidUsername: true,
//     passwordValue: '',
//     isValidPassword: true,
//     isRememberMeChecked: false,
//     isAuthenticated: false
//   };
// }
const Login: React.FunctionComponent<IOwnProps> = (props) => {
  
  const [showHelperText, setshowHelperText] = useState(false);
  const [usernameValue, setusernameValue] = useState('');
  const [isValidUsername, setisValidUsername] = useState(true);
  const [passwordValue, setpasswordValue] = useState('');
  const [isValidPassword, setisValidPassword] = useState(true);
  const [isRememberMeChecked, setisRememberMeChecked] = useState(false);
  const [isAuthenticated, setisAuthenticated] = useState(false);

  const handleUsernameChange = value => {
    setusernameValue(value);
  };

  const handlePasswordChange = passwordValue => {
    setpasswordValue(passwordValue);
  };

  const onRememberMeClick = () => {
    setisRememberMeChecked(!isRememberMeChecked);
  };

  const onLoginButtonClick = event => {
    event.preventDefault();
    setisValidUsername(!!usernameValue);
    setisValidPassword(!!passwordValue);
      setshowHelperText(! usernameValue || ! passwordValue );
    if (isValidUsername && isValidPassword) {
      if (usernameValue == 'admin' && passwordValue == 'admin') {
        setisAuthenticated(true);
      }
    }
  };

    const helperText = (
      <React.Fragment>
        <ExclamationCircleIcon />
        &nbsp;Invalid login credentials.
      </React.Fragment>
    );

    const signUpForAccountMessage = (
      <LoginMainFooterBandItem>
        Need an account? <a href="#">Sign up.</a>
      </LoginMainFooterBandItem>
    );
    const forgotCredentials = (
      <LoginMainFooterBandItem>
        <a href="#">Forgot username or password?</a>
      </LoginMainFooterBandItem>
    );

    const loginForm = (
      <LoginForm
        showHelperText={showHelperText}
        helperText={helperText}
        usernameLabel="Username"
        usernameValue={usernameValue}
        onChangeUsername={handleUsernameChange}
        isValidUsername={isValidUsername}
        passwordLabel="Password"
        passwordValue={passwordValue}
        onChangePassword={handlePasswordChange}
        isValidPassword={isValidPassword}
        rememberMeLabel="Keep me logged in for 30 days."
        isRememberMeChecked={isRememberMeChecked}
        onChangeRememberMe={onRememberMeClick}
        onLoginButtonClick={onLoginButtonClick}
      />
    );
    if (isAuthenticated) {
      return props.children;
    }
    return (
      <LoginPage
        className="pf-login"
        footerListVariants="inline"
        brandImgSrc={brandImage}
        brandImgAlt="Kogito logo"
        backgroundImgSrc={images}
        backgroundImgAlt="Images"
        loginTitle="Log in to your account"
        loginSubtitle="Please use your single sign-on LDAP credentials"
        textContent="CLOUD-NATIVE BUSINESS AUTOMATION FOR BUILDING INTELLIGENT APPLICATIONS, BACKED BY BATTLE-TESTED CAPABILITIES."
        signUpForAccountMessage={signUpForAccountMessage}
        forgotCredentials={forgotCredentials}
      >
        {loginForm}
      </LoginPage>
    );
  }

export default Login;
