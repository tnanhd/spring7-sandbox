import reactLogo from "./assets/react.svg";
import viteLogo from "./assets/vite.svg";
import heroImg from "./assets/hero.png";
import "./App.css";
import LoginButton from "./LoginButton";
import { useEffect, useState } from "react";
import { getAccessToken } from "./token";

function App() {
  const [accessToken, setAccessToken] = useState<string | null>(null);

  useEffect(() => {
    const code = new URLSearchParams(globalThis.location.search).get("code");
    globalThis.history.replaceState({}, document.title, "/");
    const codeVerifier = localStorage.getItem("code_verifier");
    localStorage.removeItem("code_verifier");

    if (!code || !codeVerifier) {
      return;
    }

    async function fetchAccessToken() {
      const tokenResponse = await getAccessToken(code!, codeVerifier!);

      if (tokenResponse.access_token) {
        setAccessToken(tokenResponse.access_token);
      }
    }

    fetchAccessToken();
  }, []);

  return (
    <section id="center">
      <div className="hero">
        <img src={heroImg} className="base" width="170" height="179" alt="" />
        <img src={reactLogo} className="framework" alt="React logo" />
        <img src={viteLogo} className="vite" alt="Vite logo" />
      </div>
      <div>
        <h1>Get started</h1>
        {accessToken ? (
          <p>You are logged in! Access token: {accessToken}</p>
        ) : (
          <LoginButton />
        )}
      </div>
    </section>
  );
}

export default App;
