const LogoutButton = () => {
  const handleLogout = () => {
    location.href =
      "http://localhost:9000/realms/myrealm/protocol/openid-connect/logout?redirect_uri=http://localhost:8080";
  };

  return (
    <button className="counter" onClick={handleLogout}>
      Logout
    </button>
  );
};

export default LogoutButton;
