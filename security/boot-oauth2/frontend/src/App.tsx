import reactLogo from "./assets/react.svg";
import viteLogo from "./assets/vite.svg";
import heroImg from "./assets/hero.png";
import "./App.css";
import LoginButton from "./LoginButton";
import { useEffect, useState } from "react";
import { getAccessToken } from "./token";
import { fetchMessages } from "./messagesService";
import LogoutButton from "./LogoutButton";

function App() {
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [message, setMessage] = useState<string>("");

  async function handleFetchMessages() {
    if (!accessToken) {
      setMessage("You need to log in first!");
      return;
    }

    const message = await fetchMessages(accessToken);
    setMessage(message);
  }

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
        {accessToken ? (
          <div>
            <p>Welcome back!</p>
            <LogoutButton />
          </div>
        ) : (
          <div>
            <h1>Get started</h1>
            <LoginButton />
          </div>
        )}
        <button className="counter" onClick={handleFetchMessages}>
          Fetch Messages
        </button>
        <p>{message}</p>
      </div>
    </section>
  );
}

export default App;
