import { useEffect, useState } from 'react';
import Navbar from './components/Navbar';
import Hero from './components/Hero';
import About from './components/About';
import Events from './components/Events';
import Team from './components/Team';
import Join from './components/Join';
import Footer from './components/Footer';
import HackathonPage from './hackathon/HackathonPage';

function useHashRoute() {
  const [hash, setHash] = useState(() => window.location.hash);

  useEffect(() => {
    const onHashChange = () => setHash(window.location.hash);
    window.addEventListener('hashchange', onHashChange);
    return () => window.removeEventListener('hashchange', onHashChange);
  }, []);

  return hash;
}

export default function App() {
  const route = useHashRoute();

  if (route.startsWith('#/hackathon')) {
    return (
      <>
        <HackathonPage />
        <Footer />
      </>
    );
  }

  return (
    <>
      <Navbar />
      <main>
        <Hero />
        <About />
        <Events />
        <Team />
        <Join />
      </main>
      <Footer />
    </>
  );
}
