import { motion, useScroll, useTransform } from "framer-motion";
import { useRef } from "react";

export default function Hero() {
  const containerRef = useRef(null);
  const { scrollYProgress } = useScroll({
    target: containerRef,
    offset: ["start start", "end start"]
  });

  // Parallax effects
  const y = useTransform(scrollYProgress, [0, 1], ["0%", "50%"]);
  const opacity = useTransform(scrollYProgress, [0, 0.8], [1, 0]);
  const scale = useTransform(scrollYProgress, [0, 1], [1, 0.9]);

  return (
    <section ref={containerRef} className="relative h-[110vh] flex items-center overflow-hidden bg-slate-50">
      {/* Background Cinematic Lighting */}
      <motion.div style={{ y, opacity, scale }} className="absolute inset-0 z-0 flex items-center justify-center">
        <div className="absolute w-[80vw] h-[80vw] md:w-[40vw] md:h-[40vw] rounded-full bg-primary-container/10 blur-[100px] top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2"></div>
      </motion.div>

      <div className="relative z-10 max-w-container-max mx-auto px-margin-mobile md:px-gutter w-full flex flex-col items-center text-center mt-20">
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1.2, ease: [0.16, 1, 0.3, 1], delay: 0.2 }}
        >
          <span className="text-primary-container font-bold tracking-widest text-xs uppercase mb-6 inline-block">
            Bengaluru's First Premium EV Network
          </span>
        </motion.div>

        <motion.h1 
          initial={{ opacity: 0, y: 60 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1.2, ease: [0.16, 1, 0.3, 1], delay: 0.4 }}
          className="font-display-lg-mobile md:font-display-lg text-[12vw] md:text-[6vw] leading-[1.1] tracking-tighter text-primary mb-8 max-w-5xl"
        >
          Help shape the future of <br />
          <span className="gradient-text italic">Bengaluru Mobility by Yaatra EV</span>
        </motion.h1>

        <motion.p 
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, ease: [0.16, 1, 0.3, 1], delay: 0.6 }}
          className="font-body-lg text-xl md:text-2xl text-text-secondary mb-12 max-w-2xl font-light"
        >
          Experience Bengaluru's premium EV-only mobility platform. Cleaner rides. Safer journeys. A better future.
        </motion.p>

        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, ease: [0.16, 1, 0.3, 1], delay: 0.8 }}
          className="flex flex-col sm:flex-row gap-6"
        >
          <a href="#waitlist" className="px-10 py-5 bg-primary-container text-on-primary font-bold rounded-full electric-glow text-lg transition-transform hover:scale-105 active:scale-95 text-center">
            Join the Waitlist
          </a>
          <a href="#vision" className="px-10 py-5 bg-transparent border border-glass-border/40 hover:border-primary-container/40 text-primary font-bold rounded-full transition-all text-lg backdrop-blur-md text-center">
            Explore the Vision
          </a>
        </motion.div>
      </div>
      
      {/* Scroll indicator */}
      <motion.div 
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 1.5, duration: 1 }}
        style={{ opacity: useTransform(scrollYProgress, [0, 0.1], [1, 0]) }}
        className="absolute bottom-12 left-1/2 -translate-x-1/2 flex flex-col items-center gap-2"
      >
        <span className="text-xs text-text-secondary uppercase tracking-widest">Scroll</span>
        <div className="w-px h-12 bg-glass-border relative overflow-hidden">
          <motion.div 
            animate={{ y: ["-100%", "100%"] }}
            transition={{ repeat: Infinity, duration: 1.5, ease: "linear" }}
            className="absolute top-0 w-full h-full bg-primary-container"
          />
        </div>
      </motion.div>
    </section>
  );
}
