import { useEffect } from 'react';
import Lenis from 'lenis';

import Navbar from './components/Navbar';
import Hero from './components/Hero';
import Storytelling from './components/Storytelling';
import VehicleShowcase from './components/VehicleShowcase';
import Bengaluru from './components/Bengaluru';
import MarketAnalysis from './components/MarketAnalysis';
import Impact from './components/Impact';
import Waitlist from './components/Waitlist';
import Footer from './components/Footer';

export default function App() {
  useEffect(() => {
    // Initialize Lenis smooth scroll
    const lenis = new Lenis({
      duration: 1.2,
      easing: (t) => Math.min(1, 1.001 - Math.pow(2, -10 * t)), // easeOutQuart
      direction: 'vertical',
      gestureDirection: 'vertical',
      smooth: true,
      mouseMultiplier: 1,
      smoothTouch: false,
      touchMultiplier: 2,
    });

    function raf(time) {
      lenis.raf(time);
      requestAnimationFrame(raf);
    }

    requestAnimationFrame(raf);

    return () => {
      lenis.destroy();
    };
  }, []);

  return (
    <div className="font-body-md antialiased text-white bg-background min-h-screen selection:bg-primary-container selection:text-on-primary">
      <Navbar />
      <Hero />
      <Storytelling />
      <VehicleShowcase />
      <Bengaluru />
      <MarketAnalysis />
      <Impact />
      <Waitlist />
      <Footer />
    </div>
  );
}
