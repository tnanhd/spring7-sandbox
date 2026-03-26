import { useEffect, useState } from "react";
import { generateCodeVerifierAndChallenge } from "./pkce";

const LoginButton = () => {
  const [codeVerifier, setCodeVerifier] = useState("");
  const [codeChallenge, setCodeChallenge] = useState("");

  useEffect(() => {
    (async () => {
      const { codeVerifier, codeChallenge } = await generateCodeVerifierAndChallenge();
      setCodeVerifier(codeVerifier);
      setCodeChallenge(codeChallenge);
    })();
  }, []);

  const handleLogin = () => {
    const params = new URLSearchParams({
      client_id: "myclient",
      redirect_uri: "http://localhost:8080",
      response_type: "code",
      scope: "openid profile email",
      code_challenge_method: "S256",
      code_challenge: codeChallenge,
    });
    localStorage.setItem("code_verifier", codeVerifier);

    globalThis.location.href = `http://localhost:9000/realms/myrealm/protocol/openid-connect/auth?${params.toString()}`;
  };

  return (
    <div>
      <button className="counter" onClick={handleLogin}>Login</button>
    </div>
  );
};

export default LoginButton;
