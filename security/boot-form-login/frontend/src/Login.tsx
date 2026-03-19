import { type SubmitEvent, useState } from "react";
import "./Login.css";
import { login } from "./api/auth";
import type { AxiosError } from "axios";

type LoginProps = {
  onSuccess: () => void;
};

export default function Login({ onSuccess }: LoginProps) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: SubmitEvent) {
    event.preventDefault();
    setError(null);
    setIsSubmitting(true);

    try {
      await login(username, password);
      onSuccess();
    } catch (error) {
      const err = error as Error | AxiosError;
      setError(err.message);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="login">
      <div className="login__card">
        <h1 className="login__title">Sign in</h1>
        <p className="login__subtitle">Use your credentials to log in.</p>
        <form className="login__form" onSubmit={handleSubmit}>
          <label className="login__field">
            <span className="login__label">Username</span>
            <input
              className="login__input"
              value={username}
              onChange={(event) => setUsername(event.target.value)}
              placeholder="e.g. janedoe"
              required
              autoComplete="username"
            />
          </label>

          <label className="login__field">
            <span className="login__label">Password</span>
            <input
              className="login__input"
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="••••••••"
              required
              autoComplete="current-password"
            />
          </label>

          <button className="login__submit" disabled={isSubmitting}>
            {isSubmitting ? "Signing in…" : "Sign in"}
          </button>
        </form>

        {error ? (
          <p className="login__error" role="alert">
            {error}
          </p>
        ) : null}
      </div>
    </main>
  );
}
