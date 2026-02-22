import "../css/Header.css";

function Header({ adMsg }) {
  return (
    <header className="top-container">
      <h1 className="site-title">LINKLITE</h1>
      <p className="ad-msg">{adMsg}</p>
          <button className="login authBtns">Login</button>
          <button className="signup authBtns">Sign Up</button>

    </header>
  );
}


export default Header;